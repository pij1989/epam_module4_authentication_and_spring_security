package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.GiftCertificateRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GiftCertificateRepositoryCustomImpl implements GiftCertificateRepositoryCustom {
    private static final String FIND_GIFT_CERTIFICATES_BY_TAG_NAMES_SQL = "SELECT gc.* FROM tags t JOIN gift_certificate_tags gct ON gct.tag_id = t.id JOIN gift_certificates gc ON gc.id = gct.gift_certificate_id WHERE t.name = ?";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean addTagToCertificate(Long certificateId, Long tagId) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, certificateId);
        if (giftCertificate != null) {
            Tag tag = entityManager.find(Tag.class, tagId);
            if (tag != null) {
                giftCertificate.addTag(tag);
                entityManager.flush();
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<GiftCertificate> updatePart(GiftCertificate giftCertificate) {
        GiftCertificate foundGiftCertificate = entityManager.find(GiftCertificate.class, giftCertificate.getId());
        if (foundGiftCertificate == null) {
            return Optional.empty();
        }
        if (giftCertificate.getName() != null) {
            foundGiftCertificate.setName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            foundGiftCertificate.setDescription(giftCertificate.getDescription());
        }
        if (giftCertificate.getPrice() != null) {
            foundGiftCertificate.setPrice(giftCertificate.getPrice());
        }
        if (giftCertificate.getDuration() != null) {
            foundGiftCertificate.setDuration(giftCertificate.getDuration());
        }
        if (giftCertificate.getCreateDate() != null) {
            foundGiftCertificate.setCreateDate(giftCertificate.getCreateDate());
        }
        if (giftCertificate.getLastUpdateDate() != null) {
            foundGiftCertificate.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        }
        entityManager.flush();
        return Optional.of(foundGiftCertificate);
    }

    @Override
    public Optional<GiftCertificate> updatePrice(long id, BigDecimal price) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if (giftCertificate != null) {
            giftCertificate.setPrice(price);
            entityManager.flush();
            return Optional.of(giftCertificate);
        }
        return Optional.empty();
    }

    @Override
    public Page<GiftCertificate> findGiftCertificateByTagNames(String[] tagNames, Pageable pageable) {
        PageImpl<GiftCertificate> page = new PageImpl<>(Collections.emptyList());
        long totalElements = countGiftCertificateByTagNames(tagNames);
        if (totalElements > 0) {
            String sqlQuery = createSqlForFindByTags(tagNames);
            System.out.println(sqlQuery);
            Query query = entityManager.createNativeQuery(sqlQuery, GiftCertificate.class)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());
            setParameters(tagNames, query);
            @SuppressWarnings("unchecked")
            List<GiftCertificate> giftCertificates = (List<GiftCertificate>) query.getResultList();
            page = new PageImpl<>(giftCertificates, pageable, totalElements);
        }
        return page;
    }

    private long countGiftCertificateByTagNames(String[] tagNames) {
        String sqlQuery = createSqlForCountByTags(tagNames);
        Query query = entityManager.createNativeQuery(sqlQuery);
        setParameters(tagNames, query);
        BigInteger result = (BigInteger) query.getSingleResult();
        return result.longValue();
    }

    private String createSqlForFindByTags(String[] tagNames) {
        StringBuilder stringBuilder = new StringBuilder(FIND_GIFT_CERTIFICATES_BY_TAG_NAMES_SQL);
        int count = 0;
        while (count < tagNames.length - 1) {
            stringBuilder.append(" INTERSECT ").append(FIND_GIFT_CERTIFICATES_BY_TAG_NAMES_SQL);
            count++;
        }
        stringBuilder.append(" ORDER BY id");
        return stringBuilder.toString();
    }

    private String createSqlForCountByTags(String[] tagNames) {
        String sqlQuery = createSqlForFindByTags(tagNames);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT count(*) FROM (").append(sqlQuery).append(") AS result");
        return stringBuilder.toString();
    }

    private void setParameters(String[] tagNames, Query query) {
        int count = 1;
        for (String tagName : tagNames) {
            query.setParameter(count, tagName);
            count++;
        }
    }
}
