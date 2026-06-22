package com.community.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // MyBatis-Plus 的核心插件，后面分页功能也要靠它
        return  new MybatisPlusInterceptor();
    }
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        //拦截实体类的 @TableField(fill = ...) 注解，自动给createdAt、updatedAt 填上当前时间，这样你写代码时就不用手动 set 这两个字段
        return new MetaObjectHandler(){
            @Override
        public void insertFill(MetaObject metaObject){
            this.strictInsertFill(metaObject,"createdAt",
                    LocalDateTime.class,LocalDateTime.now());
            this.strictInsertFill(metaObject,"createdAt",
                    LocalDateTime.class,LocalDateTime.now());
            }
            @Override
            public  void updateFill(MetaObject metaObject){
                this.strictUpdateFill(metaObject,"updateAt",
                        LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}
