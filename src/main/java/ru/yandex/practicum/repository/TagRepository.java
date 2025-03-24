package ru.yandex.practicum.repository;

import java.util.Optional;
import ru.yandex.practicum.dao.Tag;

/**
 * Получение данных из таблицы Tags.
 */
public interface TagRepository {

  /**
   * Поиск тега по наименованию.
   *
   * @param tagName - наименование тега.
   * @return объект тега.
   */
  Optional<Tag> findDistinctByTagName(String tagName);

  /**
   * Сохранение тега в БД.
   *
   * @param tag - объект тега.
   * @return результат сохранения с id.
   */
  Tag save(Tag tag);

  /**
   * Поиск тега по id.
   *
   * @param id - id тега.
   * @return найденный тег.
   */
  Optional<Tag> findById(Long id);
}
