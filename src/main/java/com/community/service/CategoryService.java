package com.community.service;

import com.community.entity.Category;

import java.util.List;

/**
 * 分类业务接口
 *  listAll() 返回 List<Category> 而不是分页，因为分类数量很少（一般几十个以内），前端一次拿到全量做下拉框。
 */
public interface CategoryService {

    List<Category> listAll(); // 列出所有分类, 用于前台显示

    Category getById(Long id); // 按id查询分类, 用于后台管理

    Category create(String name,Integer sort); // 创建分类, 用于后台管理

    Category update(Long id, String name,Integer sort); // 更新分类, 用于后台管理

    void delete(Long id); // 删除分类, 用于后台管理
}
