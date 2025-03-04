package ru.yandex.practicum.repository;

import java.util.List;
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
  List<Post> findAllPosts(Pageable pageable);

  /**
   * Получение поста по id.
   *
   * @param id - id поста.
   * @return пост.
   */
  Post findById(Long id);

  /**
   * Удаление поста из БД по id.
   *
   * @param id - id поста.
   */
  void deletePostById(Long id);
}
