package com.community.controller;

import com.community.common.Result;
import com.community.dto.LoginRequest;
import com.community.dto.RegisterRequest;
import com.community.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  //返回 JSON，不走页面跳转
@RequestMapping("/api/user")  //这个控制器的所有接口都以/api/user 开头
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    /**
     * 用户注册
     * Valid — 触发 RegisterRequest 和 LoginRequest 里的@NotBlank 校验
     *  RequestBody注解 — 从 HTTP 请求体里读取 JSON 并转成 Java对象
     */
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterRequest request){
        return Result.success(userService.register(request));
    }
    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest request){
        return Result.success(userService.login(request));
    }
}
