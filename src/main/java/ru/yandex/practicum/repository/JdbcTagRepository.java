package ru.yandex.practicum.repository;

import java.util.Set;
import ru.yandex.practicum.dao.Tag;

/**
 * Получение данных из таблицы Tags.
 */
public class JdbcTagRepository  {
  /**
   * Получение всех тегов по id из коллекции.
   *
   * @param ids - коллекция id.
   * @return коллекция тегов.
   */
  Set<Tag> findAllByIdIn(Set<Long> ids);
}
