package ru.yandex.practicum.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dao.Post;

/**
 * Получение данных из таблицы Posts.
 */
public interface JdbcPostRepository {
  /**
   * Получение всех постов.
   *
   * @param pageable - данные для пагинации.
   * @return список постов.
   */
  Page<Post> findAllPosts(Pageable pageable);

  Optional<Post> findById(Long id);

  Long save(Post post);

  void update (Post post);

  void deleteById(Long id);
}