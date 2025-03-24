package ru.yandex.practicum.dao;

import java.util.List;

import lombok.*;

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
  private String imagePath;
  private String text;
  private List<Comment> comments;
  private Integer likesCount;
}
