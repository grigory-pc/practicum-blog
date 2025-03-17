package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.dao.Post;

/**
 * Получение данных из таблицы Posts.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

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
  @Query("UPDATE Post p SET p.countLikes = p.countLikes + 1 WHERE p.id = :postId")
  void increaseLikesCount(@Param("postId") Long postId);
}
