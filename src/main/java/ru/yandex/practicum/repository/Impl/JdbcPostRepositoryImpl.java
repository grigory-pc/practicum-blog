package ru.yandex.practicum.repository.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.repository.JdbcPostRepository;

/**
 * Получение данных из таблицы Posts.
 */
@Repository
@RequiredArgsConstructor
public class JdbcPostRepositoryImpl implements JdbcPostRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public Page<Post> findAllPosts(Pageable pageable) {

    String countSql = "SELECT COUNT(*) FROM posts";
    int total = jdbcTemplate.queryForObject(countSql, Integer.class);

    String querySql = "SELECT id, title, image, text " +
                      "FROM posts " +
                      "ORDER BY id " +
                      "LIMIT ? OFFSET ?";

    List<Post> posts = jdbcTemplate.query(
        querySql,
        new Object[] {pageable.getPageSize(), pageable.getOffset()},
        (rs, rowNum) -> {
          Post post = new Post();
          post.setId(rs.getLong("id"));
          post.setTitle(rs.getString("title"));
          post.setImage(rs.getBytes("image"));
          post.setText(rs.getString("text"));
          return post;
        }
    );
    return new PageImpl<>(posts, pageable, total);
  }

  @Override
  public Optional<Post> findById(Long id) {
    String sql = "SELECT id, title, image, text FROM posts WHERE id = ?";

    try {
      Post post = jdbcTemplate.queryForObject(
          sql,
          new Object[] {id},
          (rs, rowNum) -> {
            Post postFromDb = new Post();
            postFromDb.setId(rs.getLong("id"));
            postFromDb.setTitle(rs.getString("title"));
            postFromDb.setImage(rs.getBytes("image"));
            postFromDb.setText(rs.getString("text"));
            return postFromDb;
          }
      );
      return Optional.of(post);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Long save(Post post) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    // Выполняем вставку с сохранением ключа
    jdbcTemplate.update(
        new PreparedStatementCreator() {
          @Override
          public PreparedStatement createPreparedStatement(Connection connection) throws
                                                                                  SQLException {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO posts(title, image, text) VALUES (?, ?, ?)",
                new String[] {"id"} // указываем имя колонки автоинкремента
            );
            ps.setString(1, post.getTitle());
            ps.setBytes(2, post.getImage());
            ps.setString(3, post.getText());
            return ps;
          }
        },
        keyHolder
    );

    return (Long) keyHolder.getKey();
  }

  @Override
  public void update(Post post) {
    jdbcTemplate.update("insert into posts(id, title, image, text) values(?, ?, ?, ?)",
                        post.getId(), post.getTitle(), post.getImage(), post.getText());
  }

  @Override
  public void deleteById(Long id) {
    jdbcTemplate.update("delete from posts where id = ?", id);
  }
}