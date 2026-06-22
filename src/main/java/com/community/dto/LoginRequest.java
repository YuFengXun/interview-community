package com.community.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
/**
 * 登录请求参数
 * ：LoginRequest 是客户端登录时发来的请求体。
 * @NotBlank 是参数校验注解
 * 如果客户端没传这些字段，Spring 会自动返回错误，不需要你手动判断。
 */
@Data
public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
