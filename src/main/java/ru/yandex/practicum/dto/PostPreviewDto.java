package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;

/**
 * DTO превью поста.
 *
 * @param id - id поста.
 * @param title -названия поста.
 * @param image - картинка.
 * @param postText - коротко первый абзац.
 * @param countComments - количество комментариев к посту.
 * @param countLikes - количество лайков к посту.
 * @param tags - множество id тегов поста.
 */
@Builder
public record PostPreviewDto(@JsonProperty(value = "id") Long id,
                             @JsonProperty(value = "title", required = true) @NotBlank String title,
                             @JsonProperty(value = "image") byte[] image,
                             @JsonProperty(value = "text",
                                           required = true) @NotBlank String postText,
                             @JsonProperty(value = "count_comments",
                                           required = true) @NotNull Integer countComments,
                             @JsonProperty(value = "count_likes",
                                           required = true) @NotNull Integer countLikes,
                             @JsonProperty(value = "tags", required = true) Set<Long> tags) {
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PostPreviewDto that = (PostPreviewDto) object;
    return Objects.equals(id, that.id) && Objects.equals(title, that.title)
           && Arrays.equals(image, that.image) && Objects.equals(postText,
                                                                 that.postText)
           && Objects.equals(countComments, that.countComments) && Objects.equals(
        countLikes, that.countLikes) && Objects.equals(tags, that.tags);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, title, postText, countComments, countLikes, tags);
    result = 31 * result + Arrays.hashCode(image);
    return result;
  }

  @Override
  public String toString() {
    return "PostPreviewDto{" +
           "id=" + id +
           ", title='" + title + '\'' +
           ", image=" + Arrays.toString(image) +
           ", postText='" + postText + '\'' +
           ", countComments=" + countComments +
           ", countLikes=" + countLikes +
           ", tags=" + tags +
           '}';
  }
}