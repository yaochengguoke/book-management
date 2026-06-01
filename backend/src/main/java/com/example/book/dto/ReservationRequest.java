package com.example.book.dto;

import javax.validation.constraints.NotNull;

public class ReservationRequest {
    @NotNull private Long bookId;
    @NotNull private Long userId;

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
