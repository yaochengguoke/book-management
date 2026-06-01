package com.example.book.controller;

import com.example.book.dto.ApiResponse;
import com.example.book.dto.ReservationRequest;
import com.example.book.entity.*;
import com.example.book.repository.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository resvRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public ReservationController(ReservationRepository resvRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.resvRepo = resvRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Map<String,Object>>> getByUser(@PathVariable Long userId) {
        List<Map<String,Object>> result = new ArrayList<>();
        for (Reservation r : resvRepo.findByUserIdOrderByCreatedAtDesc(userId)) {
            Map<String,Object> m = new HashMap<>();
            m.put("id", r.getId());
            m.put("bookId", r.getBook().getId());
            m.put("bookTitle", r.getBook().getTitle());
            m.put("status", r.getStatus());
            m.put("queuePosition", resvRepo.countByBookIdAndStatus(r.getBook().getId(), "WAITING"));
            m.put("createdAt", r.getCreatedAt());
            m.put("notifiedAt", r.getNotifiedAt());
            result.add(m);
        }
        return ApiResponse.ok(result);
    }

    @PostMapping
    public ApiResponse<Map<String,Object>> create(@Valid @RequestBody ReservationRequest req) {
        Book book = bookRepo.findById(req.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepo.findById(req.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (resvRepo.existsByUserIdAndBookIdAndStatus(req.getUserId(), req.getBookId(), "WAITING")) {
            throw new RuntimeException("您已预约过该图书，请勿重复预约");
        }

        Reservation r = new Reservation();
        r.setBook(book); r.setUser(user);
        r.setStatus("WAITING");
        r = resvRepo.save(r);

        Map<String,Object> m = new HashMap<>();
        m.put("id", r.getId());
        m.put("bookTitle", book.getTitle());
        m.put("status", r.getStatus());
        m.put("queuePosition", resvRepo.countByBookIdAndStatus(book.getId(), "WAITING"));
        return ApiResponse.ok(m);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> cancel(@PathVariable Long id) {
        Reservation r = resvRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        r.setStatus("CANCELLED");
        resvRepo.save(r);
        return ApiResponse.ok(null);
    }
}
