package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CommentDto;

/**
 * Сервис для работы с комментариями.
 */
public interface CommentService {

  /**
   * Добавление комментария.
   *
   * @param postId - id поста.
   */
  void saveComment(Long postId, CommentDto comment);

  /**
   * Обновление комментария.
   *
   * @param id - id поста.
   * @param commentId - id комментария.
   * @param comment - данные комментария.
   */
  void updateComment(Long id, Long commentId, CommentDto comment);

  /**
   * Удаление комментария по id.
   *
   * @param id - id комментария.
   */
  void deleteCommentById(Long id);
}