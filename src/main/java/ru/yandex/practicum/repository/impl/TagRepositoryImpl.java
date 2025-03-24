package ru.yandex.practicum.repository.impl;

import jakarta.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    try {
      return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{tagName}, rowMapper));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Transactional
  @Override
  public Tag save(Tag tag) {
    String insertSql = "INSERT INTO tags (tag_name) VALUES (?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, tag.getTagName());
      return ps;
    }, keyHolder);

    Long id = keyHolder.getKey().longValue();

    String selectSql = "SELECT tag_name FROM tags WHERE id = ?";

    RowMapper<Tag> rowMapper = (ResultSet rs, int rowNum) -> {
      Tag savedTag = new Tag();
      savedTag.setId(id);
      savedTag.setTagName(rs.getString("tag_name"));
      return savedTag;
    };

    return jdbcTemplate.queryForObject(selectSql, rowMapper, id);
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
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[] {id}, rowMapper)
      );
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}