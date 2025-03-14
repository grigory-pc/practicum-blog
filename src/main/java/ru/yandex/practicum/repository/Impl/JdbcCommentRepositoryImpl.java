package ru.yandex.practicum.repository.Impl;


import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.repository.JdbcCommentRepository;

/**
 * Получение данных из таблицы Comments.
 */
@Repository
@RequiredArgsConstructor
public class JdbcCommentRepositoryImpl implements JdbcCommentRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public void save(Comment comment) {
    jdbcTemplate.update("insert into comments(post_id, text) values(?, ?)",
                        comment.getPostId(), comment.getText());
  }

  @Override
  public Optional<Comment> findById(Long id) {
    String sql = "SELECT id, post_id, text FROM comments WHERE id = ?";

    try {
      Comment comment = jdbcTemplate.queryForObject(
          sql,
          new Object[] {id},
          (rs, rowNum) -> {
            Comment commentFromDb = new Comment();
            commentFromDb.setId(rs.getLong("id"));
            commentFromDb.setPostId(rs.getLong("post_id"));
            commentFromDb.setText(rs.getString("text"));
            return commentFromDb;
          }
      );
      return Optional.of(comment);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
  @Override
  public void deleteById(Long id) {
    jdbcTemplate.update("delete from comments where id = ?", id);
  }
}