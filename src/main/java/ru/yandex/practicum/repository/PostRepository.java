package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.Post;

/**
 * Получение данных из таблицы Posts.
 */
public interface PostRepository extends JpaRepository<Post, Integer> {

  /**
   * Получение всех постов.
   *
   * @param pageable - данные для пагинации.
   * @return список постов.
   */
  Page<Post> findAllPosts(Pageable pageable);

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
