package com.example.book.service;

import com.example.book.config.WeChatConfig;
import com.example.book.dto.LoginResponse;
import com.example.book.entity.User;
import com.example.book.repository.UserRepository;
import com.example.book.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class WeChatService {

    private static final String WX_CODE2SESSION_URL =
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    private final WeChatConfig weChatConfig;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public WeChatService(WeChatConfig weChatConfig, UserRepository userRepository,
                         PasswordEncoder passwordEncoder, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.weChatConfig = weChatConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    public LoginResponse wxLogin(String code, String nickName, String avatarUrl) {
        // 1. Call WeChat API to get openid
        String openid = getOpenIdFromWeChat(code);
        if (openid == null || openid.isEmpty()) {
            throw new RuntimeException("微信登录失败：无法获取openid");
        }

        // 2. Find user by openid or create new user
        String wxUsername = "wx_" + openid.substring(0, 10);
        User user = userRepository.findByUsername(wxUsername).orElse(null);

        if (user == null) {
            user = new User();
            user.setUsername(wxUsername);
            user.setPassword(passwordEncoder.encode(openid)); // use openid as password
            user.setRole("USER");
            user = userRepository.save(user);
        }

        // 3. Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return new LoginResponse(token, user.getUsername(), user.getRole());
    }

    private String getOpenIdFromWeChat(String code) {
        try {
            String urlStr = String.format(WX_CODE2SESSION_URL,
                    weChatConfig.getAppId(), weChatConfig.getAppSecret(), code);

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8");
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            JsonNode json = objectMapper.readTree(response);

            if (json.has("errcode") && json.get("errcode").asInt() != 0) {
                System.err.println("WeChat API error: " + response);
                return null;
            }

            return json.get("openid").asText();

        } catch (Exception e) {
            System.err.println("WeChat login error: " + e.getMessage());
            return null;
        }
    }
}
