package com.example.book.dto;

import javax.validation.constraints.*;

public class ReviewRequest {
    @NotNull private Long bookId;
    @NotNull private Long userId;
    @NotNull @Min(1) @Max(5) private Integer rating;
    private String content;

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
