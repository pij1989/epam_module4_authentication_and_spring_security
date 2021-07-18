package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class GiftCertificateServiceImplTest {
    private GiftCertificate giftCertificate;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @BeforeEach
    void setUp() {
        giftCertificate = new GiftCertificate();
        giftCertificate.setName("New gift certificate name");
        giftCertificate.setDescription("New gift certificate description");
        giftCertificate.setPrice(new BigDecimal("55.77"));
        giftCertificate.setDuration(30);
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        giftCertificate = null;
    }

    @Test
    void createGiftCertificate() {
        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
        Optional<GiftCertificate> actual = giftCertificateService.createGiftCertificate(giftCertificate);
        verify(giftCertificateRepository, times(1)).save(giftCertificate);
        assertEquals(Optional.of(giftCertificate), actual);
    }

    @Test
    void findGiftCertificate() {
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificate));
        Optional<GiftCertificate> actual = giftCertificateService.findGiftCertificate(1L);
        assertEquals(Optional.of(giftCertificate), actual);
    }

    @Test
    void findAllGiftCertificate() {
        when(giftCertificateRepository.findAll()).thenReturn(List.of(new GiftCertificate(), new GiftCertificate()));
        List<GiftCertificate> actual = giftCertificateService.findAllGiftCertificate();
        verify(giftCertificateRepository, times(1)).findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void updateGiftCertificate() {
        giftCertificate.setName("Updated gift certificate name");
        giftCertificate.setDescription("Updated gift certificate description");
        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificate));
        Optional<GiftCertificate> actual = giftCertificateService.updateGiftCertificate(giftCertificate, 1L);
        verify(giftCertificateRepository, times(1)).save(giftCertificate);
        assertEquals(Optional.of(giftCertificate), actual);
    }

    @Test
    void createTagInGiftCertificate() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Tag name");
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(new GiftCertificate()));
        when(giftCertificateRepository.addTagToCertificate(1L, 1L)).thenReturn(true);
        when(tagRepository.save(tag)).thenReturn(tag);
        Optional<Tag> actual = giftCertificateService.createTagInGiftCertificate(1L, tag);
        assertEquals(Optional.of(tag), actual);
    }

    @Test
    void addTagToGiftCertificate() {
        when(giftCertificateRepository.addTagToCertificate(1L, 1L)).thenReturn(true);
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(new GiftCertificate()));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(new Tag()));
        boolean condition = giftCertificateService.addTagToGiftCertificate(1L, 1L);
        verify(giftCertificateRepository, times(1)).addTagToCertificate(1L, 1L);
        assertTrue(condition);
    }

    @Test
    void deleteGiftCertificate() {
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificate));
        boolean condition = giftCertificateService.deleteGiftCertificate(1L);
        verify(giftCertificateRepository, times(1)).delete(giftCertificate);
        assertTrue(condition);
    }

    @Test
    void findGiftCertificateByTagName() {
        String name = "name";
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(new GiftCertificate());
        Page<GiftCertificate> page = new PageImpl<>(giftCertificates);
        when(giftCertificateRepository.findAllByTagName(name, PageRequest.of(0,5))).thenReturn(page);
        Page<GiftCertificate> actual = giftCertificateService.findGiftCertificateByTagName(name, 1, 5);
        assertEquals(page, actual);
    }

    @Test
    void searchGiftCertificate() {
        String name = "name";
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(new GiftCertificate());
        Page<GiftCertificate> page = new PageImpl<>(giftCertificates);
        when(giftCertificateRepository.findAllByTagName(name, PageRequest.of(0,5))).thenReturn(page);
        Page<GiftCertificate> actual = giftCertificateService.findGiftCertificateByTagName(name, 1, 5);
        assertEquals(page, actual);
    }
}