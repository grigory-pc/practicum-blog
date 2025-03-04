package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.PostTag;

/**
 * Получение данных из таблицы tags-posts.
 */
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
