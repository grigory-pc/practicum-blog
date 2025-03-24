package ru.yandex.practicum.repository;

import java.util.Optional;
import ru.yandex.practicum.dao.Comment;

/**
 * Получение данных из таблицы Comments.
 */
public interface CommentRepository {

  void save(Comment comment);

  Optional<Comment> findById(Long id);

  void deleteById(Long id);
}
