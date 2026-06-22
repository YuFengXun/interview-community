package com.community.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")  //  告诉 MyBatis-Plus 这个类对应数据库的 user 表
public class User {
    @TableId(type = IdType.AUTO) //主键策略为自增
    private Long id;

    // 数据库字段
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatar;

    @TableLogic //逻辑删除，之后调用 delete 方法时不会真删，而是把 deleted 字段设为 1
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT) //自动填充时间（稍后配置）
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE) //自动填充时间（稍后配置）
    private LocalDateTime updatedAt;

}
