package com.epam.esm.model.repository;

import com.epam.esm.model.entity.Tag;

import java.util.Optional;

public interface TagRepositoryCustom {
    Optional<Tag> findMaxCountTagByUserId(Long userId);
}
