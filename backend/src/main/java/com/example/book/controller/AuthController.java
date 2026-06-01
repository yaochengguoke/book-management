package com.example.book.controller;

import com.example.book.dto.ApiResponse;
import com.example.book.dto.LoginRequest;
import com.example.book.dto.LoginResponse;
import com.example.book.dto.WxLoginRequest;
import com.example.book.service.AuthService;
import com.example.book.service.WeChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final WeChatService weChatService;

    public AuthController(AuthService authService, WeChatService weChatService) {
        this.authService = authService;
        this.weChatService = weChatService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/wx-login")
    public ApiResponse<LoginResponse> wxLogin(@Valid @RequestBody WxLoginRequest request) {
        return ApiResponse.ok(weChatService.wxLogin(
                request.getCode(), request.getNickName(), request.getAvatarUrl()));
    }
}
