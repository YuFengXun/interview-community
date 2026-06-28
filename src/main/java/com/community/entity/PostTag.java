package com.community.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("post_tag")
public class PostTag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long postId;

    private Long tagId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
