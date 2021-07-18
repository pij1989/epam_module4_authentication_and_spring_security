package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.TagRepository;
import com.epam.esm.model.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Optional<Tag> create(Tag tag) {
        if (tag != null) {
            return Optional.of(tagRepository.save(tag));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Tag> findTag(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public List<Tag> findAllTag() {
        return tagRepository.findAll();
    }

    @Override
    @Transactional
    public boolean deleteTag(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            tagRepository.delete(tag);
            return true;
        }
        return false;
    }

    @Override
    public Page<Tag> findTags(int page, int size) {
        if (page == 0 || size == 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        return tagRepository.findAllByOrderByIdAsc(PageRequest.of(page - 1, size));
    }
}
