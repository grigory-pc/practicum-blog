package ru.yandex.practicum.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Класс комбинации пост-тега.
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@IdClass(PostTagKey.class)
@Table(name = "posts_tags")
public class PostTag {
  @Id
  @Column(name = "post_id", nullable = false)
  private Long postId;
  @Id
  @Column(name = "tag_id", nullable = false)
  private Long tagId;
}