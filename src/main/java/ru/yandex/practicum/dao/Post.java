package ru.yandex.practicum.dao;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.*;

import java.util.List;

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
  private long id;
  @Column(name = "title", nullable = false)
  private String title;
  @Column(name = "image", nullable = false)
  private String image;
  @Column(name = "text", nullable = false)
  private String text;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "post_id")
  private List<Comment> comments;
  @Column(name = "likes", nullable = false, columnDefinition = "integer default 0")
  private int likes;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "post_id")
  private List<Tag> tags;
}
