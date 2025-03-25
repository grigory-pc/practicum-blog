package ru.yandex.practicum.repository;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.dao.Comment;

/**
 * Получение данных из таблицы Comments.
 */
public interface CommentRepository {

  /**
   * Сохранение комментария в БД.
   *
   * @param comment - объект комментария.
   */
  void save(Comment comment);

  /**
   * Обновление комментария в БД.
   *
   * @param comment - объект комментария.
   */
  void update(Comment comment);

  /**
   * Получение комментария по id.
   * @param id - id комментария.
   *
   * @return объект комментария.
   */
  Optional<Comment> findById(Long id);

  /**
   * Удаление комментария по id.
   *
   * @param id - id комментария.
   */
  void deleteById(Long id);

  /**
   * Получение списка комментариев по post id
   *
   * @param postId - id поста.
   * @return список комментариев.
   */
  List<Comment> findAllByPostId(Long postId);

  /**
   * Удаление всех комментариев по post id.
   *
   * @param postId - id комментария.
   */
  void deleteCommentsByPostId(Long postId);
}