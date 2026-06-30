package com.community.controller;

import com.community.common.Result;
import com.community.dto.CreatePostRequest;
import com.community.dto.PostVO;
import com.community.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * 创建帖子
     * Valid — 触发 CreatePostRequest 里的@NotBlank 校验
     *  RequestBody注解 — 从 HTTP 请求体里读取 JSON 并转成 Java对象
     */
    @PostMapping
    public Result<PostVO> create(@Valid @RequestBody CreatePostRequest request){
        Long userId = getCurrentUserId();
        return Result.success(postService.createPost(userId, request));// 创建帖子
    }
    /**
     * 根据id查询帖子
     */
    @GetMapping("/{id}")
        public Result<PostVO> getById(@PathVariable Long id){
            // 获取当前用户ID
            return Result.success(postService.getPostById(id));
        }
    /**
     * 修改帖子
     */

    @PutMapping("/{id}")
    public Result<PostVO> update(@PathVariable Long id, @Valid @RequestBody CreatePostRequest request) {
        Long userId = getCurrentUserId();
        return Result.success(postService.updatePost(id, userId, request));
    }
    /**
     * 删除帖子
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        postService.deletePost(id, userId);
        return Result.success();
    }
    /**
     * 帖子列表
     */
    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                      @RequestParam(defaultValue = "10") int size,
                      @RequestParam(required = false)
                      Long categoryId) {
    return Result.success(postService.listPosts(page, size, categoryId));
    }

    /**
     * 按标题搜索帖子
     */
    @GetMapping("/search")
    public Result<?> search(@RequestParam String keyword,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size) {
        return Result.success(postService.searchByTitle(keyword, page, size));
    }

    /**
     * 热门帖子排行榜（按浏览量排序）
     */
    @GetMapping("/hot")
    public Result<?> hot(@RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        return Result.success(postService.listHotPosts(page, size));
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
