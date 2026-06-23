package com.community.config;


import com.community.common.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * 继承 OncePerRequestFilter — 确保每次请求只经过一次
 * 从 Authorization 请求头里提取 Bearer xxx 格式的 token
 * jwtUtil.validateToken(token) 校验 JWT 是否合法
 * 合法就把 userId 放进 SecurityContextHolder，这样 Controller里就能通过 getCurrentUserId() 拿到
 * 不合法或没有 token →什么都不做，请求继续往下走。如果是需要登录的接口，Spring Security
 * 会自动返回 401
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtUtil jwtUtil;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String header=request.getHeader("Authorization");
        if(StringUtils.hasText( header)&& header.startsWith("Bearer ")){
            // 从header中取出token
            String token=header.substring(7);// 从第7个字符开始截取
            if(jwtUtil.validateToken(token)){
                // 验证token
                Long userId=jwtUtil.getUserIdFromToken(token); // 从token中获取用户ID
                UsernamePasswordAuthenticationToken auth=
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList()); // 创建认证信息
                SecurityContextHolder.getContext().setAuthentication(auth); // 设置认证信息
            }
        }
        filterChain.doFilter(request, response);
    }
}
