package ru.yandex.practicum.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import ru.yandex.practicum.dao.Tag;

/**
 * Получение данных из таблицы Tags.
 */
public interface JdbcTagRepository {
  /**
   * Получение всех тегов по id из коллекции.
   *
   * @param ids - коллекция id.
   * @return коллекция тегов.
   */
  Set<Tag> findAllByIdIn(Set<Long> ids);

  List<Tag> findAll();

  Optional<Tag> findById(Long id);
}
