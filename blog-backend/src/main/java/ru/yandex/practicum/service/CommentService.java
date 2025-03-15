package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.exceptions.NotFoundException;

/**
 * Сервис для работы с комментариями.
 */
public interface CommentService {

  /**
   * Добавление комментария.
   *
   * @param postId - id поста.
   * @param comment - комментарий для сохранения.
   * @throws ru.yandex.practicum.exceptions.NotFoundException - исключение в случае, если в базе данных не найдена запись.
   */
  void saveComment(Long postId, CommentDto comment) throws NotFoundException;

  /**
   * Обновление комментария.
   *
   * @param commentId - id комментария.
   * @param comment - данные комментария.
   * @throws ru.yandex.practicum.exceptions.NotFoundException - исключение в случае, если в базе данных не найдена запись.
   */
  void updateComment(Long id, Long commentId, CommentDto comment) throws NotFoundException;

  /**
   * Удаление комментария по id.
   *
   * @param id - id комментария.
   */
  void deleteCommentById(Long id);
}