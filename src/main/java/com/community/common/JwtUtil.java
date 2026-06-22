package com.community.common;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类
 * 用户登录成功后，服务端生成一个加密的 token返回给客户端。
 * 客户端后续请求在 Header 里带上这个token，
 * 服务端就能知道是谁在请求，不需要每次都查数据库。这叫无状态认证。
 */
@Component
public class JwtUtil {

    private final SecretKey key; // 密钥
    private final long expiration;  // 过期时间

    public JwtUtil(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
        }
    // 生成 JWT
    public String generate(Long userId, String username) {
        Date now=new Date();
        return Jwts.builder()
                .subject(userId.toString())  // 主体：用户 ID
                .claim("username", username)  // 额外信息：用户名
                .issuedAt( now)   // 签发时间
                .expiration(new Date(now.getTime() + expiration)) // 设置过期时间
                .signWith(key)  // 签名
                .compact();
        }
        // 解析 JWT ，从token解析出用户ID
    public Long getUserIdFromToken(String token) {
        Claims claims=Jwts.parser() // 解析器
                .verifyWith(key) // 验证密钥
                .build() // 构建解析器
                .parseSignedClaims(token) // 解析 JWT
                .getPayload();  // 获取负载
        return Long.parseLong(claims.getSubject()); // 获取用户 ID
        }
        //验证JWT是否有效
    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token); // 解析 JWT
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            // JwtException 是 JWT 的异常
            return false;
        }
    }

}
