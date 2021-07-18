package com.epam.esm.model.repository;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>, GiftCertificateRepositoryCustom {
    boolean addTagToCertificate(Long certificateId, Long tagId);

    Optional<GiftCertificate> updatePart(GiftCertificate giftCertificate);

    Optional<GiftCertificate> updatePrice(long id, BigDecimal price);

    Page<GiftCertificate> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    Page<GiftCertificate> findGiftCertificateByTagNames(String[] tagNames, Pageable pageable);

    Page<GiftCertificate> findAllByOrderByIdAsc(Pageable pageable);

    @Query(value = "SELECT gc FROM gift_certificates gc JOIN gc.tags t WHERE t.name = :name order by gc.id")
    Page<GiftCertificate> findAllByTagName(@Param("name") String name, Pageable pageable);
}
