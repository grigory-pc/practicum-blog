package ru.yandex.practicum.repository;

import java.util.Optional;
import ru.yandex.practicum.dao.Like;

/**
 * Получение данных из таблицы Likes.
 */
public class JdbcLikeRepository{
  /**
   * Получение лайков для определенного id поста.
   *
   * @param id - id поста.
   * @return объект лайков.
   */
  Optional<Like> findByPostId(Long id);
}
