package ru.yandex.practicum.service;

import ru.yandex.practicum.exceptions.NotFoundException;

/**
 * Сервис для работы с лайками.
 */
public interface LikeService {

  /**
   * Создание записи лайка к новому посту.
   *
   * @param id - id поста.
   */
  void saveLike(Long id);

  /**
   * Добавление лайка к посту.
   *
   * @param id - id поста.
   * @throws ru.yandex.practicum.exceptions.NotFoundException - исключение в случае, если в базе данных не найдена запись.
   */
  void addLike(Long id) throws NotFoundException;
}