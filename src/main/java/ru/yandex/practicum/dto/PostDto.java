package ru.yandex.practicum.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO поста.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class PostDto {
  private Long id;
  private String title;
  private String text;
  private String imagePath;
  private int likesCount;
  private List<CommentDto> comments;
  private List<String> tags;

  public String getTextPreview() {
    return text.length() > 200 ? text.substring(0, 200) + "..." : text;
  }
  public String getTagsAsText() {
    return tags.stream()
               .map(tag -> "#" + tag)
               .collect(Collectors.joining(", "));
  }
}