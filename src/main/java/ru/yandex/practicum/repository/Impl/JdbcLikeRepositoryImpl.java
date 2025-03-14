package ru.yandex.practicum.repository.Impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.Like;
import ru.yandex.practicum.repository.JdbcLikeRepository;

/**
 * Получение данных из таблицы Likes.
 */
@Repository
@RequiredArgsConstructor
public class JdbcLikeRepositoryImpl implements JdbcLikeRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public Optional<Like> findByPostId(Long id) {
    String sql = "SELECT id, post_id, count_likes FROM likes WHERE post_id = ?";

    try {
      Like like = jdbcTemplate.queryForObject(
          sql,
          new Object[] {id},
          (rs, rowNum) -> {
            Like likeFromDb = new Like();
            likeFromDb.setId(rs.getLong("id"));
            likeFromDb.setPostId(rs.getLong("post_id"));
            likeFromDb.setCountLikes(rs.getInt("count_likes"));
            return likeFromDb;
          }
      );
      return Optional.of(like);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void save(Like like) {
    jdbcTemplate.update("insert into likes(post_id, count_likes) values(?, ?)",
                        like.getPostId(), like.getCountLikes());
  }

  @Override
  public void incrementLikes(Long postId) {
    String sql = "UPDATE likes SET count_likes = count_likes + 1 WHERE post_id = ?";

    jdbcTemplate.update(sql, postId);
  }
}