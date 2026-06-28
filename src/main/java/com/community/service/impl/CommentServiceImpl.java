package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.dto.CommentVO;
import com.community.dto.CreateCommentRequest;
import com.community.entity.Comment;
import com.community.entity.Post;
import com.community.entity.User;
import com.community.mapper.CommentMapper;
import com.community.mapper.PostMapper;
import com.community.mapper.UserMapper;
import com.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final PostMapper postMapper; // 用于判断帖子是否存在
    private final UserMapper userMapper; // 用于判断用户是否存在

    /**
     * 将 Comment 转为 CommentVO
     */
    private CommentVO toCommetVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setPostId(comment.getPostId());
        vo.setUserId(comment.getUserId());
        vo.setParentId(comment.getParentId());
        vo.setContent(comment.getContent());
        vo.setCreatedAt(comment.getCreatedAt());

        User user=userMapper.selectById(comment.getUserId());
        if (user != null)vo.setAuthorNickname(user.getNickname()); // 查作者昵称等于作者用户名
        return vo;
    }
    @Override
    public CommentVO createComment(Long userId, CreateCommentRequest request) {
        // 检查帖子是否存在
        Post post=postMapper.selectById(request.getPostId());
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        if(request.getParentId()!=null){
            Comment parent=commentMapper.selectById(request.getParentId());
            if(parent==null){
                throw new RuntimeException("回复的评论不存在");
            }
        }
        // 创建评论
        Comment comment = new Comment();
        comment.setPostId(request.getPostId());
        comment.setUserId(userId);
        comment.setParentId(request.getParentId());
        comment.setContent(request.getContent());
        commentMapper.insert(comment); // 插入数据库
        return toCommetVO(comment);
    }

    @Override
    public void deleteComment(Long id, Long userId) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("没有权限");
        }
        commentMapper.deleteById(id);  // 逻辑删除
    }

    @Override
    public IPage<CommentVO> listByPost(Long postId,Long parentId, int page, int size) {

        Page<Comment> pageRequest = new Page<>(page,size);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId); // 按帖子id查询
        if(parentId !=null){
            wrapper.eq("parent_id", parentId);
        }else {
            wrapper.isNull("parent_id");  // 按父级id查询
        }
        wrapper.orderByDesc("created_at"); // 按创建时间倒序
        IPage<Comment> commentPage = commentMapper.selectPage(pageRequest, wrapper); // 分页查询
        return commentPage.convert(this::toCommetVO);
    }
}
