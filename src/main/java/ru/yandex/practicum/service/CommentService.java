package ru.yandex.practicum.service;

import ru.yandex.practicum.exceptions.NotFoundException;

/**
 * Сервис для работы с комментариями.
 */
public interface CommentService {

  /**
   * Сохранение комментария.
   *
   * @param postId - id поста.
   * @param text - текст комментария для сохранения.
   * @throws NotFoundException - исключение в случае, если в базе данных не найдена запись.
   */
  void saveComment(Long postId, String text) throws NotFoundException;

  /**
   * Удаление комментария по id.
   *
   * @param id - id комментария.
   */
  void deleteCommentById(Long id);
}