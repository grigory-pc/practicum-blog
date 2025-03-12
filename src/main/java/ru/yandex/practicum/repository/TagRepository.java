package ru.yandex.practicum.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.Tag;

/**
 * Получение данных из таблицы Tags.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
  /**
   * Получение всех тегов по id из коллекции.
   *
   * @param ids - коллекция id.
   * @return коллекция тегов.
   */
  Set<Tag> findAllByIdIn(Set<Long> ids);
}
