package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.Like;

/**
 * Получение данных из таблицы Likes.
 */
public interface LikeRepository extends JpaRepository<Like, Long> {
  /**
   * Получение лайков для определенного id поста.
   *
   * @param id - id поста.
   * @return объект лайков.
   */
  Like findByPostId(Long id);
}
