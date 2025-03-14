package ru.yandex.practicum.repository.Impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.repository.JdbcTagRepository;

/**
 * Получение данных из таблицы Tags.
 */
@Repository
@RequiredArgsConstructor
public class JdbcTagRepositoryImpl implements JdbcTagRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public Set<Tag> findAllByIdIn(Set<Long> ids) {
    if (ids.isEmpty()) {
      return Collections.emptySet();
    }

    String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));

    String sql = "SELECT id, tag_name " +
                 "FROM tags " +
                 "WHERE id IN (" + placeholders + ")";

    List<Tag> tags = jdbcTemplate.query(
        sql,
        ids.toArray(new Long[0]),
        (rs, rowNum) -> {
          Tag tag = new Tag();
          tag.setId(rs.getLong("id"));
          tag.setTagName(rs.getString("tag_name"));
          return tag;
        }
    );

    return new HashSet<>(tags);
  }

  @Override
  public List<Tag> findAll() {

    return jdbcTemplate.query(
        "select id, tag_name from tags",
        (rs, rowNum) -> new Tag(
            rs.getLong("id"),
            rs.getString("tag_name")
        ));
  }

  @Override
  public Optional<Tag> findById(Long id) {
    String sql = "SELECT id, tag_name FROM tags WHERE id = ?";

    try {
      Tag tag = jdbcTemplate.queryForObject(
          sql,
          new Object[] {id},
          (rs, rowNum) -> {
            Tag tagFromDb = new Tag();
            tagFromDb.setId(rs.getLong("id"));
            tagFromDb.setTagName(rs.getString("tag_name"));
            return tagFromDb;
          }
      );
      return Optional.of(tag);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}