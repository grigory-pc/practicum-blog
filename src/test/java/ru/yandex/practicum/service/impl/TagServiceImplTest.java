package ru.yandex.practicum.service.impl;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.TagService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TagServiceImpl.class)
class TagServiceImplTest {
  private static final String TAG_2024 = "TAG_2024";
  private static final String TAG_2025 = "TAG_2025";
  @MockitoBean
  TagRepository tagRepository;
  @MockitoBean
  TagMapper tagMapper;
  @Autowired
  TagService tagService;

  @Test
  void positiveTest_ShouldFindAllTags() {
    try {
      when(tagRepository.findAll())
          .thenReturn(
              List.of(new Tag(1L, TAG_2024),
                      new Tag(2L, TAG_2025)));
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