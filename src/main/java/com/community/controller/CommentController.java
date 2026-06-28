package com.community.controller;

import com.community.common.Result;
import com.community.dto.CommentVO;
import com.community.dto.CreateCommentRequest;
import com.community.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
/**
 * 创建评论
 */
    @PostMapping
    public Result<CommentVO> create(@Valid @RequestBody CreateCommentRequest request) {
        Long userId = getCurrentUserId();
        return Result.success(commentService.createComment(userId, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        commentService.deleteComment(id, userId);
        return Result.success();
    }

    @GetMapping
    public Result<?> listByPost(@RequestParam Long postId,
                                @RequestParam(required = false) Long parentId,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size) {
        return Result.success(commentService.listByPost(postId, parentId, page, size));
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
