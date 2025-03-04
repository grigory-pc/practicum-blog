package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
 * @param tags - теги поста.
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
                             @JsonProperty(value = "tags", required = true) List<TagDto> tags) {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostPreviewDto that = (PostPreviewDto) o;
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

  /**
   * Собирает строковое JSON-представление сущности.
   *
   * @return JSON представление сущности, сгенерировано с помощью авто-генерации кода версии 1.0.2
   * @see <a href="https://gitlab.ebsbio.tech/nbp/utils/intelij-idea-templates">Проект с шаблонами
   * для генерации кода</a>
   */
  @Override
  public String toString() {
    return "{\"_class\":\"ru.yandex.practicum.dto.PostPreviewDto\""
           + ", \"id\": " + id
           + ", \"title\": " + (title == null ? null : '"' + title + '"')
           + ", \"image\": " + (image == null ? null
                                              : "{\"blob\": {\"size\": " + image.length + "}}")
           + ", \"postText\": " + (postText == null ? null : '"' + postText + '"')
           + ", \"countComments\": " + countComments
           + ", \"countLikes\": " + countLikes
           + ", \"tags\": " + (tags == null ? null : (tags).stream()
                                                           .map(Objects::toString)
                                                           .collect(
                                                               Collectors.joining(
                                                                   ", ", "[", "]")))
           + "}";
  }
}