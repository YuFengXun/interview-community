package com.community.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotNull(message = "帖子ID不能为空")
    private Long postId;

    private Long parentId;  // 父级评论的id, 为空表示

    @NotNull(message = "评论内容不能为空")
    private String content;
}
