package ru.yandex.practicum.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.Comment;

/**
 * Получение данных из таблицы Comments.
 */
@Repository
@RequiredArgsConstructor
public class JdbcCommentRepository {
  private final JdbcTemplate jdbcTemplate;

  public void save(Comment comment) {
    jdbcTemplate.update("insert into comments(post, last_name, age, active) values(?, ?, ?, ?)",
                        user.getFirstName(), user.getLastName(), user.getAge(), user.isActive());
  }
}
