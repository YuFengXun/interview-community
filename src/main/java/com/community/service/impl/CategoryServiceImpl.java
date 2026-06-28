package com.community.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.entity.Category;
import com.community.mapper.CategoryMapper;
import com.community.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类业务实现类
 * getById 单独抽出来做存在性校验，update 和 delete 复用
 * sort 用 Integer 而不是 int，允许不传时用 0
 * orderByAsc("sort") 按 sort 字段升序排列
 */
@Service // @Service — 这是一个服务类，是 Service 接口的实现类
@RequiredArgsConstructor  // @RequiredArgsConstructor — 创建一个构造函数，参数为 final 修饰的属性
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    @Override
    public List<Category> listAll() {
        // TODO: 获取所有分类, 用于前台显示
        return categoryMapper.selectList(
                new QueryWrapper<Category>().orderByAsc("sort"));
        // 获取所有分类，按排序字段升序排列
    }

    @Override
    public Category getById(Long id) {
        Category category = categoryMapper.selectById(id); // 按 id 查询
        if (category == null){
            throw new RuntimeException("分类不存在");
        }
        return category;
    }

    @Override
    public Category create(String name, Integer sort) {
        Category category = new Category(); // 创建分类对象
        category.setName(name); // 分类名称
        category.setSort(sort != null ? sort : 0); // 分类排序字段，默认为 0
        categoryMapper.insert(category); // 插入数据库
        return category;
    }

    @Override
    public Category update(Long id, String name, Integer sort) {
        Category category = getById(id); // 按 id 查询, 判断分类是否存在
        category.setName(name);
        if (sort != null) category.setSort(sort);
        categoryMapper.updateById(category);
        return category;
    }

    @Override
    public void delete(Long id) {
        Category category = getById(id);
        categoryMapper.deleteById(id);  // 逻辑删除
    }
}
