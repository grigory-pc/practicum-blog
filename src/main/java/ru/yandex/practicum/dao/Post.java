package ru.yandex.practicum.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

import lombok.*;

/**
 * Класс поста.
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "title", nullable = false)
  private String title;
  @Column(name = "image_path")
  private String imagePath;
  @Column(name = "text", nullable = false)
  private String postText;
  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "post_id")
  private List<Comment> comments;
  @Column(name = "count_likes", nullable = false)
  private Integer likesCount;
}
