package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.Comment;

/**
 * Получение данных из таблицы Comments.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
