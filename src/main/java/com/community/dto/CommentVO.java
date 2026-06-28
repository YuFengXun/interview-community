package com.community.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String authorNickname; //额外查询的字段
}
