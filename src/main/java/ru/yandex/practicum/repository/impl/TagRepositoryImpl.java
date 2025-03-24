package ru.yandex.practicum.repository.impl;

import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.repository.TagRepository;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public Optional<Tag> findDistinctByTagName(String tagName) {
    String sql = "SELECT DISTINCT id, tag_name FROM tags WHERE tag_name = ?";

    RowMapper<Tag> rowMapper = (ResultSet rs, int rowNum) -> {
      Tag tag = new Tag();
      tag.setId(rs.getLong("id"));
      tag.setTagName(rs.getString("tag_name"));
      return tag;
    };

    return Optional.ofNullable(
        jdbcTemplate.queryForObject(sql, new Object[] {tagName}, rowMapper)
    );
  }

  @Transactional
  @Override
  public Tag save(Tag tag) {
    String insertSql = "INSERT INTO tags (tag_name) VALUES (?)";
    String selectSql = "SELECT id, tag_name FROM tags WHERE id = LAST_INSERT_ID()";

    jdbcTemplate.update(insertSql, tag.getTagName());

    RowMapper<Tag> rowMapper = (ResultSet rs, int rowNum) -> {
      Tag savedTag = new Tag();
      savedTag.setId(rs.getLong("id"));
      savedTag.setTagName(rs.getString("tag_name"));
      return savedTag;
    };

    return jdbcTemplate.queryForObject(selectSql, rowMapper);
  }

  @Override
  public Optional<Tag> findById(Long id) {
    String sql = "SELECT id, tag_name FROM tags WHERE id = ?";

    RowMapper<Tag> rowMapper = (ResultSet rs, int rowNum) -> {
      Tag tag = new Tag();
      tag.setId(rs.getLong("id"));
      tag.setTagName(rs.getString("tag_name"));
      return tag;
    };

    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(sql, new Object[] {id}, rowMapper)
      );
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}