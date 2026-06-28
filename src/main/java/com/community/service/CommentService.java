package com.community.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.community.dto.CommentVO;
import com.community.dto.CreateCommentRequest;

/**
 * 评论业务接口
 */
public interface CommentService {
    CommentVO createComment(Long userId, CreateCommentRequest request);
    void deleteComment(Long commentId, Long userId);
    IPage<CommentVO> listByPost(Long postId, Long parentId,int page, int size); // 分页查询
}
