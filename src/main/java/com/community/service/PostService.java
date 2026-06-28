package com.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.community.dto.CreatePostRequest;
import com.community.dto.PostVO;


/**
 * 帖子业务类
 * createPost 需要 userId 参数——从 JWT 里拿到的当前登录用户
 * updatePost 和 deletePost 除了 id 还要传 userId，用于校验"只能操作自己的帖子"
 * IPage<PostVO> 是 MyBatis-Plus 的分页对象，自带 total、pages 等分页信息
 */
public interface PostService {

    PostVO createPost(Long userId, CreatePostRequest request); // 创建帖子

    PostVO getPostById(Long id);  // 获取帖子

    PostVO updatePost(Long id,Long userId,CreatePostRequest request);  // 更新帖子

    void deletePost(Long id,Long userId);  // 删除帖子

    IPage<PostVO> listPosts(int page,int size,Long categoryId);

    IPage<PostVO> searchByTitle(String keyword, int page, int size); // 按标题搜索，带分页

    IPage<PostVO> listHotPosts(int page, int size);// 获取最热帖子，带分页

}
