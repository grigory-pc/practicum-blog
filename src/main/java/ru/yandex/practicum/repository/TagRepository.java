package ru.yandex.practicum.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.Tag;

/**
 * Получение данных из таблицы Tags.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

  /**
   * Поиск тега по наименованию.
   * @param tagName - наименование тега.
   * @return объект тега.
   */
  Optional<Tag> findDistinctByTagName(String tagName);
}
