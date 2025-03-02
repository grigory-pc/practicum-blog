package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.dao.Post;

/**
 * Сервис для работы с постами.
 */
public interface PostService {

  /**
   * Получение всех постов.
   *
   * @param from - с какой страницы
   * @param size - количество записей.
   * @return список постов.
   */
  Page<Post> findAllPosts(int from, int size);

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