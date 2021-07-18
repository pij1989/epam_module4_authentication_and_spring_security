package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.TagRepository;
import com.epam.esm.model.service.GiftCertificateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    public static final Logger logger = LoggerFactory.getLogger(GiftCertificateServiceImpl.class);
    private static final String SORT_NAME = "name";
    private static final String SORT_CREATE_DATE = "createDate";
    private static final String DEFAULT_SORT = "id";
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> createGiftCertificate(GiftCertificate giftCertificate) {
        if (giftCertificate != null) {
            return Optional.of(giftCertificateRepository.save(giftCertificate));
        }
        return Optional.empty();
    }

    @Override
    public Optional<GiftCertificate> findGiftCertificate(Long id) {
        return giftCertificateRepository.findById(id);
    }

    @Override
    public List<GiftCertificate> findAllGiftCertificate() {
        return giftCertificateRepository.findAll();
    }

    @Override
    public Page<GiftCertificate> findGiftCertificates(int page, int size) {
        if (page == 0 || size == 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        return giftCertificateRepository.findAllByOrderByIdAsc(PageRequest.of(page - 1, size));
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> updateGiftCertificate(GiftCertificate giftCertificate, Long id) {
        if (giftCertificate == null) {
            return Optional.empty();
        }
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        if (optionalGiftCertificate.isPresent()) {
            giftCertificate.setId(id);
            return Optional.of(giftCertificateRepository.save(giftCertificate));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> updatePartGiftCertificate(GiftCertificate giftCertificate, Long id) {
        if (giftCertificate == null) {
            return Optional.empty();
        }
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        if (optionalGiftCertificate.isPresent()) {
            giftCertificate.setId(id);
            return giftCertificateRepository.updatePart(giftCertificate);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Tag> createTagInGiftCertificate(Long certificateId, Tag tag) {
        if (tag != null && giftCertificateRepository.findById(certificateId).isPresent()) {
            Tag createdTag = tagRepository.save(tag);
            Long tagId = createdTag.getId();
            return giftCertificateRepository.addTagToCertificate(certificateId, tagId) ? Optional.of(createdTag) : Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean addTagToGiftCertificate(Long certificateId, Long tagId) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(certificateId);
        if (optionalGiftCertificate.isPresent()) {
            if (tagRepository.findById(tagId).isPresent()) {
                return giftCertificateRepository.addTagToCertificate(certificateId, tagId);
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteGiftCertificate(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate giftCertificate = optionalGiftCertificate.get();
            giftCertificateRepository.delete(giftCertificate);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Page<GiftCertificate> findGiftCertificateByTagName(String name, int page, int size) {
        if (page == 0 || size == 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        return giftCertificateRepository.findAllByTagName(name, PageRequest.of(page - 1, size));
    }

    @Override
    @Transactional
    public Page<GiftCertificate> searchGiftCertificate(String filter, int page, int size) {
        if (page == 0 || size == 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        return giftCertificateRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(filter, filter, PageRequest.of(page - 1, size));
    }

    @Override
    @Transactional
    public Page<GiftCertificate> sortGiftCertificate(String sort, String order, int page, int size) {
        if (page == 0 || size == 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        Sort.Direction direction;
        try {
            direction = Sort.Direction.valueOf(order.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid order of sorting: " + order, e);
            direction = Sort.Direction.ASC;
        }
        if (!sort.equals(SORT_NAME) && !sort.equals(SORT_CREATE_DATE)) {
            sort = DEFAULT_SORT;
        }
        return giftCertificateRepository.findAll(PageRequest.of(page - 1, size, Sort.by(direction, sort)));
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> updatePriceGiftCertificate(BigDecimal price, long id) {
        if (price == null) {
            return Optional.empty();
        }
        return giftCertificateRepository.updatePrice(id, price);
    }

    @Override
    public Page<GiftCertificate> searchGiftCertificateByTags(String[] tagNames, int page, int size) {
        PageImpl<GiftCertificate> giftCertificatePage = new PageImpl<>(Collections.emptyList());
        if (page == 0 || size == 0) {
            return giftCertificatePage;
        }
        return giftCertificateRepository.findGiftCertificateByTagNames(tagNames, PageRequest.of(page - 1, size));
    }
}
