package ru.yandex.practicum.repository.impl;

import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.repository.PostTagRepository;

@Repository
@RequiredArgsConstructor
public class PostTagRepositoryImpl implements PostTagRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<PostTag> findAllByPostId(Long postId) {
    String sql = "SELECT post_id, tag_id FROM post_tag WHERE post_id = ?";

    RowMapper<PostTag> rowMapper = (ResultSet rs, int rowNum) -> {
      PostTag postTag = new PostTag();
      postTag.setPostId(rs.getLong("post_id"));
      postTag.setTagId(rs.getLong("tag_id"));
      return postTag;
    };

    return jdbcTemplate.query(sql, new Object[]{postId}, rowMapper);
  }

  @Override
  @Transactional
  public void deleteAllByPostId(Long postId) {
    String sql = "DELETE FROM post_tag WHERE post_id = ?";
    jdbcTemplate.update(sql, postId);
  }
}