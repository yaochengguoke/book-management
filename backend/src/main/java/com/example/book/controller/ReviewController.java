package com.example.book.controller;

import com.example.book.dto.ApiResponse;
import com.example.book.dto.ReviewRequest;
import com.example.book.dto.ReviewResponse;
import com.example.book.entity.Book;
import com.example.book.entity.Review;
import com.example.book.entity.User;
import com.example.book.repository.BookRepository;
import com.example.book.repository.ReviewRepository;
import com.example.book.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public ReviewController(ReviewRepository reviewRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.reviewRepo = reviewRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/book/{bookId}")
    public ApiResponse<List<ReviewResponse>> getByBook(@PathVariable Long bookId) {
        List<ReviewResponse> reviews = reviewRepo.findByBookId(bookId).stream().map(r -> {
            ReviewResponse resp = new ReviewResponse();
            resp.setId(r.getId()); resp.setBookId(r.getBook().getId());
            resp.setUserId(r.getUser().getId()); resp.setUsername(r.getUser().getUsername());
            resp.setRating(r.getRating()); resp.setContent(r.getContent());
            resp.setCreatedAt(r.getCreatedAt());
            return resp;
        }).collect(Collectors.toList());
        return ApiResponse.ok(reviews);
    }

    @PostMapping
    public ApiResponse<ReviewResponse> create(@Valid @RequestBody ReviewRequest req) {
        Book book = bookRepo.findById(req.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepo.findById(req.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        review.setBook(book); review.setUser(user);
        review.setRating(req.getRating()); review.setContent(req.getContent());
        review = reviewRepo.save(review);

        // Update book avg rating
        List<Review> all = reviewRepo.findByBookId(book.getId());
        double avg = all.stream().mapToInt(Review::getRating).average().orElse(0);
        book.setAvgRating(Math.round(avg * 10.0) / 10.0);
        book.setRatingCount(all.size());
        bookRepo.save(book);

        ReviewResponse resp = new ReviewResponse();
        resp.setId(review.getId()); resp.setBookId(book.getId());
        resp.setUserId(user.getId()); resp.setUsername(user.getUsername());
        resp.setRating(review.getRating()); resp.setContent(review.getContent());
        resp.setCreatedAt(review.getCreatedAt());
        return ApiResponse.ok(resp);
    }
}
