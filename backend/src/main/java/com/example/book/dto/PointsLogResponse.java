package com.example.book.dto;

import java.time.LocalDateTime;

public class PointsLogResponse {

    private Long id;
    private Long userId;
    private String username;
    private Integer amount;
    private String type;
    private String typeText;
    private String description;
    private Integer balanceBefore;
    private Integer balanceAfter;
    private LocalDateTime createdAt;

    public PointsLogResponse() {}

    public PointsLogResponse(Long id, Long userId, String username, Integer amount, 
                            String type, String typeText, String description,
                            Integer balanceBefore, Integer balanceAfter, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.amount = amount;
        this.type = type;
        this.typeText = typeText;
        this.description = description;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTypeText() { return typeText; }
    public void setTypeText(String typeText) { this.typeText = typeText; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getBalanceBefore() { return balanceBefore; }
    public void setBalanceBefore(Integer balanceBefore) { this.balanceBefore = balanceBefore; }
    public Integer getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(Integer balanceAfter) { this.balanceAfter = balanceAfter; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}