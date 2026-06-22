package com.community.service;

import com.community.dto.LoginRequest;
import com.community.dto.LoginResponse;
import com.community.dto.RegisterRequest;
import com.community.entity.User;

/**
 * 用户服务接口
 * 接口定义了三个方法：注册、登录、查用户。接口的好处是
 * 后面想换实现（比如加缓存），只需要加一个新实现类，Controller 层不用改。
 */
public interface UserService {
    // 用户注册
    LoginResponse register(RegisterRequest request); // 返回登录响应

    // 用户登录
    LoginResponse login(LoginRequest request); // 返回登录响应

    //根据ID获取用户
    User getUserById(Long id);


}
