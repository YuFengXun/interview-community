package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.dto.CreatePostRequest;
import com.community.dto.PostVO;
import com.community.entity.*;
import com.community.mapper.*;
import com.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper; // 这里需要注入
    private final PostTagMapper postTagMapper;
    private final TagMapper tagMapper;

/**
 * 把 Post 转成 PostVO
 * BeanUtils.copyProperties 是 Spring 提供的工具，把同名属性自动复制过去，省去手动 set 十几个字段
 * 这里会有 N+1 查询问题（每转一个 Post 就要查两次数据库），
 * 但帖子列表一般也就十几条，现阶段完全够用。以后优化可以用@Select 写 JOIN SQL
 */
    private PostVO toPostVO(Post post) {
        PostVO vo = new PostVO();
        BeanUtils.copyProperties(post, vo);  // 把 Post 的同名字段复制到 VO

        // 查作者昵称
        User user = userMapper.selectById(post.getUserId());
        if (user != null) vo.setAuthorNickname(user.getNickname());

        // 查分类名称
        Category category = categoryMapper.selectById(post.getCategoryId());
        if (category != null) vo.setCategoryName(category.getName());
        // 查标签名称列表
        List<PostTag> postTags = postTagMapper.selectList(
                new QueryWrapper<PostTag>().eq("post_id",
                        post.getId()));
        List<String> tagNames = postTags.stream()
                .map(pt -> {
                    Tag tag = tagMapper.selectById(pt.getTagId());
                    return tag != null ? tag.getName() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        vo.setTagNames(tagNames);

        return vo;
    }
    /**
     * 创建帖子
     */
    @Override
    public PostVO createPost(Long userId, CreatePostRequest request) {
        Post post=new Post();
        post.setUserId(userId);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategoryId(request.getCategoryId());
        post.setViewCount(0);  // 默认为0
        post.setLikeCount(0); // 默认为0
        postMapper.insert(post); // 插入数据库
        // 处理标签关联
        if (request.getTagIds() != null &&
                !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                PostTag postTag = new PostTag();
                postTag.setPostId(post.getId());
                postTag.setTagId(tagId);
                postTagMapper.insert(postTag);
            }
        }
        return toPostVO(post);  // 返回VO
    }
    /**
     * 根据id查询帖子
     */
    @Override
    public PostVO getPostById(Long id) {
        Post post=postMapper.selectById(id);
        if(post==null){
            throw new RuntimeException("帖子不存在");
        }
        //浏览量加1
        post.setViewCount(post.getViewCount()+1);
        postMapper.updateById(post);
        return toPostVO(post);
    }
    /**
     * 修改帖子
     */
    @Override
    public PostVO updatePost(Long id, Long userId, CreatePostRequest request) {
        Post post=postMapper.selectById(id);
        if(post == null){
            throw new RuntimeException("帖子不存在");
        }
        if (!post.getUserId().equals(userId)){
            // 判断当前用户是否是帖子的作者
            throw new RuntimeException("无权修改");
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategoryId(request.getCategoryId());
        postMapper.updateById(post);

        // 处理标签关联
        // 先删除旧的标签关联，再插入新的
        postTagMapper.delete(new
                QueryWrapper<PostTag>().eq("post_id", id));
        if (request.getTagIds() != null &&
                !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                PostTag postTag = new PostTag();
                postTag.setPostId(id);
                postTag.setTagId(tagId);
                postTagMapper.insert(postTag);
            }
        }

        return toPostVO(post);
    }
    /**
     * 删除帖子
     */
    @Override
    public void deletePost(Long id, Long userId) {
        Post post=postMapper.selectById(id);
        if(post == null){
            throw new RuntimeException("帖子不存在");
        }
        if (!post.getUserId().equals(userId)){
            // 删除当前用户是否是帖子的作者
            throw new RuntimeException("无权删除");
        }
        postMapper.deleteById(id);  // 逻辑删除，deleted被设为 1
    }
    /**
     * 帖子列表
     */
    @Override
    public IPage<PostVO> listPosts(int page, int size, Long categoryId) {
        Page<Post> pageRequest = new Page<>(page,size);  // 创建分页对象
        QueryWrapper<Post> wrapper =new QueryWrapper<>();
        wrapper.orderByDesc("created_at");// 按创建时间倒序
        if (categoryId != null) {wrapper.eq("category_id", categoryId);} // 按分类过滤
        IPage<Post> postPage = postMapper.selectPage(pageRequest, wrapper);
        return postPage.convert(this::toPostVO); // 把 Post 转成 PostVO
    }
    /**
     * 按标题搜索帖子
     * 模糊匹配，匹配标题或内容
     */
    @Override
    public IPage<PostVO> searchByTitle(String keyword, int page, int size) {
        Page< Post> pageRequest = new Page<>(page,size); // 创建分页对象, 默认从第一页开始
        QueryWrapper<Post> wrapper = new QueryWrapper<>();  // 创建查询条件, 按创建时间倒序
        wrapper.like("title", keyword).orderByDesc("created_at"); // 按标题模糊匹配, 按创建时间倒序
        IPage<Post> postPage= postMapper.selectPage(pageRequest, wrapper);// 查询
        return postPage.convert(this::toPostVO);// 把 Post 转成 PostVO, 返回

    }
    /**
     * 热门帖子列表
     * 按点赞数排序, 返回前10条
     * orderByDesc 传多个字段名，逗号分隔。相当于 ORDER BY view_count DESC, created_at DESC。
     */
    @Override
    public IPage<PostVO> listHotPosts(int page, int size) {
        Page< Post> pageRequest = new Page<>(page,size); // 创建分页对象, 默认从第一页开始
        QueryWrapper<Post> wrapper = new QueryWrapper<>();  // 创建查询条件, 按创建时间倒序
        wrapper.orderByDesc("view_count", "created_at"); // 按浏览数排序, 按创建时间倒序
        IPage<Post> postPage= postMapper.selectPage(pageRequest, wrapper);// 查询, 返回
        return postPage.convert(this::toPostVO);
    }

}
