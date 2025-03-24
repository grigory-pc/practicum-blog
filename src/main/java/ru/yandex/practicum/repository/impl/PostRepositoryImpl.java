package ru.yandex.practicum.repository.impl;

import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.repository.PostRepository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public Optional<Post> findById(Long id) {
    String postSql = "SELECT p.id, p.title, p.image_path, p.text, p.likes_count " +
                     "FROM posts p WHERE p.id = ?";

    String commentsSql = "SELECT c.id, c.post_id, c.text " +
                         "FROM comments c WHERE c.post_id = ?";

    RowMapper<Post> postRowMapper = (ResultSet rs, int rowNum) -> {
      Post post = new Post();
      post.setId(rs.getLong("id"));
      post.setTitle(rs.getString("title"));
      post.setImagePath(rs.getString("image_path"));
      post.setText(rs.getString("text"));
      post.setLikesCount(rs.getInt("likes_count"));
      return post;
    };

    RowMapper<Comment> commentRowMapper = (ResultSet rs, int rowNum) -> {
      Comment comment = new Comment();
      comment.setId(rs.getLong("id"));
      comment.setPostId(rs.getLong("post_id"));
      comment.setText(rs.getString("text"));
      return comment;
    };

    try {
      Post post = jdbcTemplate.queryForObject(postSql, new Object[] {id}, postRowMapper);
      List<Comment> comments = jdbcTemplate.query(commentsSql, new Object[] {id}, commentRowMapper);
      post.setComments(comments);
      return Optional.of(post);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  @Transactional
  public Optional<Post> save(Post post) {
    String insertPostSql
        = "INSERT INTO posts (title, image_path, text, likes_count) VALUES (?, ?, ?, ?)";
    String selectPostSql
        = "SELECT id, title, image_path, text, likes_count FROM posts WHERE id = LAST_INSERT_ID()";

    try {
      jdbcTemplate.update(insertPostSql,
                          post.getTitle(),
                          post.getImagePath(),
                          post.getText(),
                          post.getLikesCount());

      Long postId = jdbcTemplate.queryForObject(selectPostSql, Long.class);

      String insertCommentSql = "INSERT INTO comments (post_id, text) VALUES (?, ?)";
      for (Comment comment : post.getComments()) {
        jdbcTemplate.update(insertCommentSql, postId, comment.getText());
      }

      return findById(postId);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Page<Post> findAll(int from, int size) {
    int offset = from * size;

    String countSql = "SELECT COUNT(*) FROM posts";
    long totalElements = jdbcTemplate.queryForObject(countSql, Long.class);

    String sql = "SELECT p.id, p.title, p.image_path, p.text, p.likes_count " +
                 "FROM posts p " +
                 "LIMIT ? OFFSET ?";

    RowMapper<Post> rowMapper = (ResultSet rs, int rowNum) -> {
      Post post = new Post();
      post.setId(rs.getLong("id"));
      post.setTitle(rs.getString("title"));
      post.setImagePath(rs.getString("image_path"));
      post.setText(rs.getString("text"));
      post.setLikesCount(rs.getInt("likes_count"));
      return post;
    };

    List<Post> content = jdbcTemplate.query(sql, new Object[] {size, offset}, rowMapper);

    RowMapper<Comment> commentRowMapper = (ResultSet rs, int rowNum) -> {
      Comment comment = new Comment();
      comment.setId(rs.getLong("id"));
      comment.setPostId(rs.getLong("post_id"));
      comment.setText(rs.getString("text"));
      return comment;
    };

    for (Post post : content) {
      String commentsSql = "SELECT c.id, c.post_id, c.text " +
                           "FROM comments c " +
                           "WHERE c.post_id = ?";

      List<Comment> comments = jdbcTemplate.query(commentsSql,
                                                  new Object[] {post.getId()},
                                                  commentRowMapper);

      post.setComments(comments);
    }

    return new PageImpl<>(content, PageRequest.of(from, size), totalElements);
  }

  @Override
  @Transactional
  public void increaseLikesCount(Long postId) {
    String sql = "UPDATE posts SET likes_count = likes_count + 1 WHERE id = ?";
    jdbcTemplate.update(sql, postId);
  }

  @Override
  @Transactional
  public void decreaseLikesCount(Long postId) {
    String sql = "UPDATE posts SET likes_count = likes_count - 1 WHERE id = ?";
    jdbcTemplate.update(sql, postId);
  }

  @Override
  public Page<Post> findByTags_NameContainingIgnoreCase(String search, int from, int size) {
    int offset = from * size;

    String countSql = "SELECT COUNT(DISTINCT p.id) " +
                      "FROM posts p " +
                      "JOIN posts_tags pt ON p.id = pt.post_id " +
                      "JOIN tags t ON pt.tag_id = t.id " +
                      "WHERE LOWER(t.tag_name) LIKE CONCAT('%', LOWER(?), '%')";

    long totalElements = jdbcTemplate.queryForObject(countSql, new Object[] {search}, Long.class);

    String sql = "SELECT DISTINCT p.* " +
                 "FROM posts p " +
                 "JOIN posts_tags pt ON p.id = pt.post_id " +
                 "JOIN tags t ON pt.tag_id = t.id " +
                 "WHERE LOWER(t.tag_name) LIKE CONCAT('%', LOWER(?), '%') " +
                 "LIMIT ? OFFSET ?";

    RowMapper<Post> rowMapper = (ResultSet rs, int rowNum) -> {
      Post post = new Post();
      post.setId(rs.getLong("id"));
      post.setTitle(rs.getString("title"));
      post.setImagePath(rs.getString("image_path"));
      post.setText(rs.getString("text"));
      post.setLikesCount(rs.getInt("likes_count"));
      return post;
    };

    List<Post> content = jdbcTemplate.query(sql, new Object[] {search, size, offset}, rowMapper);

    RowMapper<Comment> commentRowMapper = (ResultSet rs, int rowNum) -> {
      Comment comment = new Comment();
      comment.setId(rs.getLong("id"));
      comment.setPostId(rs.getLong("post_id"));
      comment.setText(rs.getString("text"));
      return comment;
    };

    for (Post post : content) {
      String commentsSql = "SELECT c.id, c.post_id, c.text " +
                           "FROM comments c " +
                           "WHERE c.post_id = ?";

      List<Comment> comments = jdbcTemplate.query(commentsSql,
                                                  new Object[] {post.getId()},
                                                  commentRowMapper);

      post.setComments(comments);
    }

    return new PageImpl<>(content, PageRequest.of(from, size), totalElements);
  }

  @Override
  @Transactional
  public void deletePostById(Long id) {
    String deleteCommentsSql = "DELETE FROM comments WHERE post_id = ?";
    jdbcTemplate.update(deleteCommentsSql, id);

    String deletePostSql = "DELETE FROM posts WHERE id = ?";
    jdbcTemplate.update(deletePostSql, id);

    String deleteTagsSql = "DELETE FROM posts_tags WHERE post_id = ?";
    jdbcTemplate.update(deleteTagsSql, id);
  }

  @Override
  public List<Post> findAll() {
    String sql = "SELECT p.id, p.title, p.image_path, p.text, p.likes_count " +
                 "FROM posts p";

    RowMapper<Post> rowMapper = (ResultSet rs, int rowNum) -> {
      Post post = new Post();
      post.setId(rs.getLong("id"));
      post.setTitle(rs.getString("title"));
      post.setImagePath(rs.getString("image_path"));
      post.setText(rs.getString("text"));
      post.setLikesCount(rs.getInt("likes_count"));
      return post;
    };

    List<Post> posts = jdbcTemplate.query(sql, rowMapper);

    // Загружаем комментарии для каждого поста
    RowMapper<Comment> commentRowMapper = (ResultSet rs, int rowNum) -> {
      Comment comment = new Comment();
      comment.setId(rs.getLong("id"));
      comment.setPostId(rs.getLong("post_id"));
      comment.setText(rs.getString("text"));
      return comment;
    };

    for (Post post : posts) {
      String commentsSql = "SELECT c.id, c.post_id, c.text " +
                           "FROM comments c " +
                           "WHERE c.post_id = ?";

      List<Comment> comments = jdbcTemplate.query(commentsSql,
                                                  new Object[] {post.getId()},
                                                  commentRowMapper);

      post.setComments(comments);
    }

    return posts;
  }

}