package com.community.controller;


import com.community.common.Result;
import com.community.entity.Category;
import com.community.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> listAll() {
        return Result.success(categoryService.listAll()); // 获取所有分类, 用于前台显示
    }
    /**
     * 按id查询分类, 用于后台管理
     *  PathVariable将路径变量的值绑定到方法参数中
     */
    @GetMapping("/{id}") // 按id查询分类, 用于后台管理
    public Result<Category> getById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }
    /**
     * 创建分类, 用于后台管理
     *  &RequestParam 将请求参数绑定到方法参数中
     */
    @PostMapping
    public Result<Category> create(@RequestParam String name,
                                   @RequestParam(required = false) Integer sort){
        return Result.success(categoryService.create(name, sort));
    }
    /**
     * 更新分类, 用于后台管理
     *  &RequestParam 将请求参数绑定到方法参数中
     */
    @PutMapping("/{id}")
    public Result<Category> update(@PathVariable Long id,
                                   @RequestParam String name,
                                   @RequestParam(required = false) Integer sort){
        return Result.success(categoryService.update(id, name, sort));
    }
    /**
     * 删除分类, 用于后台管理
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }
}
