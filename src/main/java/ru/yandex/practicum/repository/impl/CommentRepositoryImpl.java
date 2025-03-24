package ru.yandex.practicum.repository.impl;

import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.repository.CommentRepository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  @Transactional
  public void save(Comment comment) {
    String sql = "INSERT INTO comments (post_id, text) VALUES (?, ?)";
    jdbcTemplate.update(sql,
                        comment.getPostId(),
                        comment.getText());
  }

  @Override
  public Optional<Comment> findById(Long id) {
    String sql = "SELECT id, post_id, text FROM comments WHERE id = ?";

    RowMapper<Comment> rowMapper = (ResultSet rs, int rowNum) -> {
      Comment comment = new Comment();
      comment.setId(rs.getLong("id"));
      comment.setPostId(rs.getLong("post_id"));
      comment.setText(rs.getString("text"));
      return comment;
    };

    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper)
      );
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    String sql = "DELETE FROM comments WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}