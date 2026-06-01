package com.example.book.dto;

import javax.validation.constraints.NotBlank;

public class WxLoginRequest {

    @NotBlank(message = "微信登录code不能为空")
    private String code;

    private String nickName;
    private String avatarUrl;

    public WxLoginRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
