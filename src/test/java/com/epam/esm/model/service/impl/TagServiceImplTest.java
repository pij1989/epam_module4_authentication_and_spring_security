package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class TagServiceImplTest {
    private Tag tag;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setId(1L);
        tag.setName("New tag name");
    }

    @AfterEach
    void tearDown() {
        tag = null;
    }

    @Test
    void create() {
        when(tagRepository.save(tag)).thenReturn(tag);
        Optional<Tag> actual = tagService.create(tag);
        verify(tagRepository, times(1)).save(tag);
        assertEquals(Optional.of(tag), actual);
    }

    @Test
    void findTag() {
        Long id = 1L;
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
        Optional<Tag> actual = tagService.findTag(id);
        assertEquals(Optional.of(tag), actual);
    }

    @Test
    void findAllTag() {
        when(tagRepository.findAll()).thenReturn(List.of(new Tag(), new Tag()));
        List<Tag> tags = tagService.findAllTag();
        verify(tagRepository,times(1)).findAll();
        assertEquals(2, tags.size());
    }

    @Test
    void deleteTag() {
        Long id = 1L;
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
        boolean condition = tagService.deleteTag(id);
        verify(tagRepository,times(1)).delete(tag);
        assertTrue(condition);
    }
}