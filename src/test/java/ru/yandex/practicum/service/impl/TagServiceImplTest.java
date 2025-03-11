package ru.yandex.practicum.service.impl;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.config.TestConfig;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.TagService;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class TagServiceImplTest {
  private static final String TAG_2024 = "TAG_2024";
  private static final String TAG_2025 = "TAG_2025";
  @Autowired
  TagRepository tagRepository;
  @Autowired
  TagMapper tagMapper;
  TagService tagService;

  @BeforeEach
  void setUp() {
    tagService = new TagServiceImpl(tagRepository, tagMapper);
  }

  @Test
  void positiveTest_ShouldFindAllTags() {
    try {

      when(tagRepository.findAll())
          .thenReturn(
              List.of(new Tag(1L, Data.getPost(), TAG_2024),
                      new Tag(2L, Data.getPost(), TAG_2025)));
      when(tagMapper.toDto(anyList()))
          .thenReturn(List.of(new TagDto(1L, TAG_2024), new TagDto(2L, TAG_2025)));

      assertDoesNotThrow(
          () -> tagService.findAllTags());

      verify(tagRepository, atLeastOnce()).findAll();
      verify(tagMapper, atLeastOnce()).toDto(anyList());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }
}