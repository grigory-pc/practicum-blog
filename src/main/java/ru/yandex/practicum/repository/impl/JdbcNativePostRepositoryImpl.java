package ru.yandex.practicum.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.List;
import ru.yandex.practicum.repository.PostRepository;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostRepositoryImpl implements PostRepository {
  private final JdbcTemplate jdbcTemplate;

  /**
   * Выполняем запрос с помощью JdbcTemplate.
   * Преобразовываем ответ с помощью RowMapper.
   *
   * @return список постов.
   */
  @Override
  public List<Post> findAllPosts() {
    return null;
  }

  @Override
  public void savePost(Post post) {
  }

  @Override
  public void deletePostById(Long id) {
  }
}