package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Post;
import java.util.List;

/**
 * Получение данных из таблицы Posts.
 */
public interface PostRepository {

  /**
   * Получение всех постов.
   *
   * @return список постов.
   */
  List<Post> findAllPosts();

  /**
   * Сохранение поста в БД.
   *
   * @param post - объект поста.
   */
  void savePost(Post post);

  /**
   * Удаление поста из БД по id.
   *
   * @param id - id поста.
   */
  void deletePostById(Long id);
}
