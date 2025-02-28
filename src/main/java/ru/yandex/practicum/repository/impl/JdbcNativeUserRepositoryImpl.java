package ru.yandex.practicum.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.User;

import java.util.List;
import ru.yandex.practicum.repository.UserRepository;

@Repository
@RequiredArgsConstructor
public class JdbcNativeUserRepositoryImpl implements UserRepository {
  private final JdbcTemplate jdbcTemplate;

  /**
   * Выполняем запрос с помощью JdbcTemplate.
   * Преобразовываем ответ с помощью RowMapper.
   *
   * @return список пользователей.
   */
  @Override
  public List<User> findAll() {
    return jdbcTemplate.query(
        "select id, first_name, last_name, age, active from users",
        (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getInt("age"),
            rs.getBoolean("active")
        ));
  }

  @Override
  public void save(User user) {
    jdbcTemplate.update("insert into users(first_name, last_name, age, active) values(?, ?, ?, ?)",
                        user.getFirstName(), user.getLastName(), user.getAge(), user.isActive());
  }

  @Override
  public void deleteById(Long id) {
    jdbcTemplate.update("delete from users where id = ?", id);
  }
}