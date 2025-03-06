package ru.yandex.practicum.service;

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
   */
  void addLike(Long id);
}