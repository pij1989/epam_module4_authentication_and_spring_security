package com.epam.esm.model.repository;

import com.epam.esm.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    Page<Tag> findAllByOrderByIdAsc(Pageable pageable);

    Optional<Tag> findMaxCountTagByUserId(Long userId);
}
