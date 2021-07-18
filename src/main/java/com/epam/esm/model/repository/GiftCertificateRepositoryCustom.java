package com.epam.esm.model.repository;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface GiftCertificateRepositoryCustom {
    boolean addTagToCertificate(Long certificateId, Long tagId);

    Optional<GiftCertificate> updatePart(GiftCertificate giftCertificate);

    Optional<GiftCertificate> updatePrice(long id, BigDecimal price);

    Page<GiftCertificate> findGiftCertificateByTagNames(String[] tagNames, Pageable pageable);
}
