package com.community.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录响应
 * 登录成功后返回的数据。token 是 JWT
 * 令牌，客户端收到后存起来，后续请求带在 Header 里证明"我是谁"。
 */
@Data // 自动生成 getter 和 setter
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String username;
    private String nickname;
    private String token; // JWT令牌



}
