package com.community.service.impl;

import com.community.common.JwtUtil;
import com.community.dto.LoginResponse;
import com.community.dto.LoginRequest;
import com.community.dto.RegisterRequest;
import com.community.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.community.service.UserService;
import org.springframework.stereotype.Service;
import com.community.entity.User;

@Service  // 服务类,告诉 Spring："这是一个业务类，把它创建成Bean，启动时加载"
@RequiredArgsConstructor // 自动注入,Lombok 的。自动给所有 final 字段生成构造方法。
public class UserServiceImpl implements  UserService{

    private final UserMapper userMapper;  // 注入 UserMapper

    private final JwtUtil jwtUtil;  //JWT生成

    private final PasswordEncoder passwordEncoder; //密码加密（BCrypt）

    /**
     * 用户注册
     * 注册成功后直接返回 token。这样用户注册完不用再调
     * 一次登录接口，前端可以直接跳转到首页。
     */
    @Override
    public LoginResponse register(RegisterRequest request) {
        //1. 检查用户名是否已存在
        User exist=userMapper.selectByUsername(request.getUsername());
        if (exist != null){
            throw new RuntimeException("用户名已被注册");
        }
        //2. 创建用户，密码加密后存进去
        User user=new User();
        user.setUsername(request.getUsername()); // 用户名
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 加密过的密码
        user.setNickname(request.getNickname() !=null ?
                request.getNickname() : request.getUsername());  // 没传昵称就用用户名
        userMapper.insert(user);  // 插入数据库
        //3. 生成 JWT,，包装成 LoginResponse 返回
        String token=jwtUtil.generate(user.getId(), user.getUsername());
        return new LoginResponse(user.getId(), user.getUsername(), user.getNickname(),token);
    }
    /**
     * 用户登录
     * 登录失败提示"用户名或密码错误"而不是"用户名不存在"或"密码错误"——这是安全规范，
     * 防止攻击者通过错误信息猜出哪些用户名已注册。
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        User user =
                userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new
                    RuntimeException("用户名或密码错误");
        }
        if
        (!passwordEncoder.matches(request.getPassword(),
                user.getPassword())) {
            throw new
                    RuntimeException("用户名或密码错误");
        }
        String token = jwtUtil.generate(user.getId(),
                user.getUsername());
        return new LoginResponse(user.getId(),
                user.getUsername(), user.getNickname(), token);
    }
    /**
     * MyBatis-Plus 的 BaseMapper 自带selectById，一行都不用写，开箱即用。
     */
    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
}
