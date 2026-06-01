package com.example.book.dto;

public class PointsResponse {

    private Long userId;
    private String username;
    private Integer balance;
    private Integer totalEarned;
    private Integer totalSpent;

    public PointsResponse() {}

    public PointsResponse(Long userId, String username, Integer balance, Integer totalEarned, Integer totalSpent) {
        this.userId = userId;
        this.username = username;
        this.balance = balance;
        this.totalEarned = totalEarned;
        this.totalSpent = totalSpent;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Integer getBalance() { return balance; }
    public void setBalance(Integer balance) { this.balance = balance; }
    public Integer getTotalEarned() { return totalEarned; }
    public void setTotalEarned(Integer totalEarned) { this.totalEarned = totalEarned; }
    public Integer getTotalSpent() { return totalSpent; }
    public void setTotalSpent(Integer totalSpent) { this.totalSpent = totalSpent; }
}