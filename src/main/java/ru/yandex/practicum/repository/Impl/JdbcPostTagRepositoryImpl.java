package ru.yandex.practicum.repository.Impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.repository.JdbcPostTagRepository;

/**
 * Получение данных из таблицы tags-posts.
 */
@Repository
@RequiredArgsConstructor
public class JdbcPostTagRepositoryImpl implements JdbcPostTagRepository {
  private final JdbcTemplate jdbcTemplate;


  @Override
  public Set<PostTag> findAllByPostIdIn(Set<Long> postIds) {
    if (postIds.isEmpty()) {
      return Collections.emptySet();
    }

    String placeholders = String.join(",", Collections.nCopies(postIds.size(), "?"));

    String sql = "SELECT post_id, tag_id " +
                 "FROM posts_tags " +
                 "WHERE id IN (" + placeholders + ")";

    List<PostTag> postTags = jdbcTemplate.query(
        sql,
        postIds.toArray(new Long[0]),
        (rs, rowNum) -> {
          PostTag postTag = new PostTag();
          postTag.setPostId(rs.getLong("post_id"));
          postTag.setTagId(rs.getLong("tag_id"));
          return postTag;
        }
    );

    return new HashSet<>(postTags);
  }

  @Override
  public Set<Long> findAllTagIdByPostId(Long postId) {
    String sql = "SELECT tag_id " +
                 "FROM post_tags " +
                 "WHERE post_id = ?";

    List<Long> tagIds = jdbcTemplate.query(
        sql,
        new Object[] {postId},
        (rs, rowNum) -> rs.getLong("tag_id")
    );
    return new HashSet<>(tagIds);
  }

  @Override
  public void deleteAllByPostId(Long postId) {
    jdbcTemplate.update("delete from post_tags where post_id = ?", postId);
  }

  @Override
  public void save(Long postId, Long tagId) {
    jdbcTemplate.update("insert into post_tags(post_id, tag_id) values(?, ?)",
                        postId, tagId);
  }
}