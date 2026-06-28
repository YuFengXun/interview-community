package com.community.dto;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子列表展示对象
 * PostVO 和 Post 实体字段几乎一样，但多了 authorNickname 和 categoryName
 * 为什么不直接用 Post 实体返回？
 * 因为实体里存的是 userId（数字），前端需要的是nickname（名字）。多两个展示字段，前端就不用再调一次接口
 * Service 层组装 PostVO 时，需要查 User 表和 Category 表把名字取出来
 */
@Data
public class PostVO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 额外查询出来的展示字段
    private String authorNickname;
    private String categoryName;

    private List<String> tagNames;  // 新增：标签名称列表
}
