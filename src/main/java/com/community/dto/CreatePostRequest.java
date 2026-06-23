package com.community.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * community@NotBlank 和 @Size 是参数校验注解，
 * Spring 在进入 Controller 之前就会校验，不通过直接返回 400
 * categoryId 没加校验注解，因为是可选字段
 */
@Data
public class CreatePostRequest {
    @NotBlank(message = "标题不能为空")
    @Size(min = 10, max = 30, message = "标题最长100个字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(max = 50000, message = "内容超出长度限制")
    private String content;

    private Long categoryId; //分类Id
}
