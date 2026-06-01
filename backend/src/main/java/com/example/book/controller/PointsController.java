package com.example.book.controller;

import com.example.book.dto.ApiResponse;
import com.example.book.dto.PointsLogResponse;
import com.example.book.dto.PointsResponse;
import com.example.book.service.PointsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/points")
public class PointsController {

    private final PointsService pointsService;

    public PointsController(PointsService pointsService) {
        this.pointsService = pointsService;
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PointsResponse> getUserPoints(@PathVariable Long userId) {
        return ApiResponse.ok(pointsService.getPointsByUserId(userId));
    }

    @PostMapping("/user/{userId}/earn")
    public ApiResponse<PointsResponse> earnPoints(
            @PathVariable Long userId,
            @RequestParam Integer amount,
            @RequestParam(defaultValue = "ADMIN_ADD") String type,
            @RequestParam(defaultValue = "") String description) {
        return ApiResponse.ok(pointsService.addPoints(userId, amount, type, description, null, null));
    }

    @PostMapping("/user/{userId}/signin")
    public ApiResponse<PointsResponse> signIn(@PathVariable Long userId) {
        return ApiResponse.ok(pointsService.signIn(userId));
    }

    @PostMapping("/user/{userId}/deduct")
    public ApiResponse<PointsResponse> deductPoints(
            @PathVariable Long userId,
            @RequestParam Integer amount,
            @RequestParam(defaultValue = "积分兑换") String description) {
        return ApiResponse.ok(pointsService.deductPoints(userId, amount, description));
    }

    @PostMapping("/user/{userId}/borrow")
    public ApiResponse<PointsResponse> earnForBorrow(
            @PathVariable Long userId,
            @RequestParam Long borrowId) {
        return ApiResponse.ok(pointsService.earnPointsForBorrow(userId, borrowId));
    }

    @PostMapping("/user/{userId}/return")
    public ApiResponse<PointsResponse> earnForReturn(
            @PathVariable Long userId,
            @RequestParam Long borrowId) {
        return ApiResponse.ok(pointsService.earnPointsForReturn(userId, borrowId));
    }

    @GetMapping("/user/{userId}/logs")
    public ApiResponse<Page<PointsLogResponse>> getUserPointsLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.ok(pointsService.getPointsLogs(userId, pageable));
    }

    @GetMapping("/rules")
    public ApiResponse<Map<String, Object>> getPointsRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("borrow", 10);
        rules.put("return", 5);
        rules.put("signin", 1);
        return ApiResponse.ok(rules);
    }
}