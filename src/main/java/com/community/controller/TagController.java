package com.community.controller;


import com.community.common.Result;
import com.community.entity.Tag;
import com.community.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public Result<List<Tag>> listAll() {
        return Result.success(tagService.listAll());
    }

    @GetMapping("/{id}")
    public Result<Tag> getById(@PathVariable Long id) {
        return Result.success(tagService.getById(id));
    }
    @PostMapping
    public Result<Tag> create(@RequestBody Tag tag) {
        return Result.success(tagService.create(tag.getName()));
    }

    @PutMapping("/{id}")
    public Result<Tag> update(@PathVariable Long id, @RequestParam String name) {
        return Result.success(tagService.update(id, name));
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        tagService.delete(id);
        return Result.success();
    }
}
