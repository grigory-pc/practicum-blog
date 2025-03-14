package ru.yandex.practicum.dao;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Класс поста.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Post {
  private Long id;
  private String title;
  private byte[] image;
  private String text;
  private Set<Comment> comments;
  private Like likes;
  private Set<Tag> tags;
}
