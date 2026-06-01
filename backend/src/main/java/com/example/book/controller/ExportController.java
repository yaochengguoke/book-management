package com.example.book.controller;

import com.example.book.dto.ApiResponse;
import com.example.book.entity.*;
import com.example.book.repository.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final BorrowRepository borrowRepo;
    private final BookRepository bookRepo;

    public ExportController(BorrowRepository borrowRepo, BookRepository bookRepo) {
        this.borrowRepo = borrowRepo;
        this.bookRepo = bookRepo;
    }

    @GetMapping("/borrows")
    public ResponseEntity<byte[]> exportBorrows() {
        List<Borrow> borrows = borrowRepo.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("﻿"); // UTF-8 BOM for Excel
        sb.append("借阅ID,图书名称,用户ID,借阅日期,应还日期,归还日期,状态\n");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Borrow b : borrows) {
            sb.append(b.getId()).append(",");
            sb.append(b.getBook().getTitle()).append(",");
            sb.append(b.getUser().getId()).append(",");
            sb.append(b.getBorrowDate() != null ? b.getBorrowDate().format(fmt) : "").append(",");
            sb.append(b.getDueDate() != null ? b.getDueDate().format(fmt) : "").append(",");
            sb.append(b.getReturnDate() != null ? b.getReturnDate().format(fmt) : "").append(",");
            sb.append(b.getStatus()).append("\n");
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("borrows.csv").build());
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String,Object>> getStats() {
        Map<String,Object> stats = new HashMap<>();
        stats.put("totalBooks", bookRepo.count());
        stats.put("totalBorrows", borrowRepo.count());
        stats.put("activeBorrows", borrowRepo.findAll().stream().filter(b -> "BORROWED".equals(b.getStatus())).count());

        // Top books
        List<Map<String,Object>> topBooks = new ArrayList<>();
        for (Book book : bookRepo.findAll()) {
            Map<String,Object> m = new HashMap<>();
            m.put("title", book.getTitle());
            m.put("borrowCount", borrowRepo.findAll().stream()
                    .filter(b -> b.getBook().getId().equals(book.getId())).count());
            topBooks.add(m);
        }
        topBooks.sort((a,b) -> Long.compare((Long)b.get("borrowCount"), (Long)a.get("borrowCount")));
        stats.put("topBooks", topBooks.subList(0, Math.min(5, topBooks.size())));

        return ApiResponse.ok(stats);
    }
}
