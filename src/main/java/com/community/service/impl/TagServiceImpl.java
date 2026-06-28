package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.entity.Tag;
import com.community.mapper.TagMapper;
import com.community.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    public List<Tag> listAll() {// 获取所有分类, 用于前台显示
        return tagMapper.selectList(new QueryWrapper<Tag>().orderByAsc("id"));
    }

    @Override
    public Tag getById(Long id) {// 获取分类
        Tag tag = tagMapper.selectById(id);
        if (tag == null){
            throw new RuntimeException("该标签不存在");
        }
        return tag;
    }

    @Override
    public Tag create(String name) {
        // 判断标签名是否已存在
        Long count = tagMapper.selectCount(new QueryWrapper<Tag>().eq("name", name));
        if (count > 0){
            throw new RuntimeException("该标签已存在");
        }
        //回显，创建成功
        Tag tag = new Tag();
        tag.setName(name);
        tagMapper.insert(tag);

        return tag;
    }

    @Override
    public Tag update(Long id, String name) {
        Tag tag = getById(id);
        tag.setName(name);
        tagMapper.updateById(tag);  // 更新, 返回更新后的数据
        return tag;
    }

    @Override
    public void delete(Long id) {
        Tag tag = getById(id);
        tagMapper.deleteById(id);
    }
}
