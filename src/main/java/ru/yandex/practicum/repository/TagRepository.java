package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.Tag;

/**
 * Получение данных из таблицы Tags.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
}
