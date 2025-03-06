package ru.yandex.practicum.service;

import java.util.List;
import ru.yandex.practicum.dto.TagDto;

/**
 * Сервис для работы с тегами.
 */
public interface TagService {

  /**
   * Получение списка всех тегов.
   *
   * @return список тегов.
   */
  List<TagDto> findAllTags();
}