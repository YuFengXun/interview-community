package com.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.community.mapper")
/**
 * 启动类
 *  Spring 默认不知道 UserMapper 这些接口在哪里。
 *  这个注解告诉 Spring："去com.community.mapper 包底下找所有的 Mapper接口，帮我把它们的实现类创建好"
 */
@SpringBootApplication
public class InterviewCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewCommunityApplication.class, args);
	}

}
