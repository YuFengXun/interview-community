package com.community.service;

import com.community.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> listAll();

    Tag getById(Long id);

    Tag create(String name);

    Tag update(Long id, String name);

    void delete(Long id);
}
