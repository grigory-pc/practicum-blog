package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Builder;

/**
 * DTO поста со всей информацией для отображения.
 *
 * @param id - id поста.
 * @param title -названия поста.
 * @param image - картинка.
 * @param postText - текст поста.
 * @param tagIds - теги поста.
 * @param comments - комментарии.
 */
@Builder
public record PostFullDto(@JsonProperty(value = "id") Long id,
                          @JsonProperty(value = "title",
                                        required = true) @NotBlank String title,
                          @JsonProperty(value = "image") byte[] image,
                          @JsonProperty(value = "post_text",
                                        required = true) @NotBlank String postText,
                          @JsonProperty(value = "tags") List<Long> tagIds,
                          @JsonProperty(value = "comments",
                                        required = true) List<CommentDto> comments) {
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFullDto that = (PostFullDto) o;
    return Objects.equals(id, that.id) && Objects.equals(title, that.title)
           && Arrays.equals(image, that.image) && Objects.equals(postText,
                                                                 that.postText)
           && Objects.equals(tagIds, that.tagIds) && Objects.equals(comments,
                                                                    that.comments);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, title, postText, tagIds, comments);
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
    return "{\"_class\":\"ru.yandex.practicum.dto.PostFullDto\""
           + ", \"id\": " + id
           + ", \"title\": " + (title == null ? null : '"' + title + '"')
           + ", \"image\": " + (image == null ? null
                                              : "{\"blob\": {\"size\": " + image.length + "}}")
           + ", \"postText\": " + (postText == null ? null : '"' + postText + '"')
           + ", \"tagIds\": " + (tagIds == null ? null : (tagIds).stream()
                                                                 .map(Objects::toString)
                                                                 .collect(
                                                                     Collectors.joining(
                                                                         ", ", "[", "]")))
           + ", \"comments\": " + (comments == null ? null : (comments).stream()
                                                                       .map(
                                                                           Objects::toString)
                                                                       .collect(
                                                                           Collectors.joining(
                                                                               ", ", "[", "]")))
           + "}";
  }
}