package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.TagRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public class TagRepositoryCustomImpl implements TagRepositoryCustom {
    private static final String FIND_MAX_COUNT_TAG_BY_USER_ID_JPQL = "SELECT t FROM users u JOIN u.orders o JOIN o.orderItems oi JOIN oi.giftCertificate gc JOIN gc.tags t WHERE u.id = :userId GROUP BY t.id ORDER BY count(t.id) DESC";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> findMaxCountTagByUserId(Long userId) {
        Tag tag = entityManager.createQuery(FIND_MAX_COUNT_TAG_BY_USER_ID_JPQL, Tag.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getSingleResult();
        return Optional.ofNullable(tag);
    }
}
