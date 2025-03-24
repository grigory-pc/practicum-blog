package ru.yandex.practicum.repository;

import java.util.List;
import ru.yandex.practicum.dao.PostTag;

/**
 * Получение данных из таблицы tags-posts.
 */
public interface PostTagRepository {
  /**
   * Поиск всех тегов по  post id.
   *
   * @param postId - post id.
   * @return коллекция связей post id и id тега.
   */
  List<PostTag> findAllByPostId(Long postId);

  /**
   * Удаление всех записей по post id.
   *
   * @param postId - id поста.
   */
  void deleteAllByPostId(Long postId);
}