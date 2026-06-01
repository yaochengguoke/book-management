package com.example.book.controller;

import com.example.book.dto.ApiResponse;
import com.example.book.dto.BorrowRequest;
import com.example.book.dto.BorrowResponse;
import com.example.book.service.BorrowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ApiResponse<Page<BorrowResponse>> getAllBorrows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ApiResponse.ok(borrowService.getAllBorrows(pageable));
    }

    @GetMapping("/search")
    public ApiResponse<Page<BorrowResponse>> searchBorrows(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isEmpty()) {
            return ApiResponse.ok(borrowService.searchByKeyword(keyword, pageable));
        }
        return ApiResponse.ok(borrowService.searchBorrows(userId, bookId, status, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<BorrowResponse> getBorrowById(@PathVariable Long id) {
        return ApiResponse.ok(borrowService.getBorrowById(id));
    }

    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getStatistics() {
        return ApiResponse.ok(borrowService.getStatistics());
    }

    @PostMapping
    public ApiResponse<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {
        return ApiResponse.ok(borrowService.borrowBook(request));
    }

    @PutMapping("/{id}/return")
    public ApiResponse<BorrowResponse> returnBook(@PathVariable Long id) {
        return ApiResponse.ok(borrowService.returnBook(id));
    }
}