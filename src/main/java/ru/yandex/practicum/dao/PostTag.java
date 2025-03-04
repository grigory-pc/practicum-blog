package ru.yandex.practicum.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Класс пост-тега.
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