package ru.yandex.practicum.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.dao.Post;

/**
 * Получение данных из таблицы Posts.
 */
public interface PostRepository extends JpaRepository<Post, Long>, CrudRepository<Post, Long> {

  /**
   * Получение всех постов.
   *
   * @param pageable - данные для пагинации.
   * @return список постов.
   */
  @Override
  Page<Post> findAll(Pageable pageable);

  /**
   * Увеличение количества лайков на 1.
   * @param postId - id поста.
   */
  @Modifying
  @Transactional
  @Query(value = "UPDATE posts SET count_likes = count_likes + 1 WHERE id = :postId",
         nativeQuery = true)
  void increaseLikesCount(@Param("postId") Long postId);
}
