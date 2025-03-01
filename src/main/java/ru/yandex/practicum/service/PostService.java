package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Post;

import java.util.List;

/**
 * Сервис для работы с постами.
 */
public interface PostService {

  /**
   * Получение списка постов.
   *
   * @return список постов.
   */
  List<Post> findPostAll();

  /**
   * Сохранение поста.
   *
   * @param post - объект поста.
   */
  void savePost(Post post);

  /**
   * Удаление поста по id.
   *
   * @param id - id поста.
   */
  void deletePostById(Long id);
}