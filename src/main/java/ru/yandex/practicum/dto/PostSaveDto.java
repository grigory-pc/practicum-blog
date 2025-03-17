package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

/**
 * DTO для добавления поста.
 *
 * @param title -названия поста.
 * @param image - картинка.
 * @param postText - текст поста.
 * @param tagIds - множество тегов поста.
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostSaveDto(@JsonProperty(value = "title",
                                        required = true) @NotBlank String title,
                          @JsonProperty(value = "image") byte[] image,
                          @JsonProperty(value = "post_text",
                                        required = true) @NotBlank String postText,
                          @JsonProperty(value = "tags") Set<Long> tagIds) {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostSaveDto that = (PostSaveDto) o;
    return Objects.equals(title, that.title) && Arrays.equals(image, that.image)
           && Objects.equals(postText, that.postText) && Objects.equals(tagIds,
                                                                        that.tagIds);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(title, postText, tagIds);
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
    return "{\"_class\":\"ru.yandex.practicum.dto.PostSaveDto\""
           + ", \"title\": " + (title == null ? null : '"' + title + '"')
           + ", \"image\": " + (image == null ? null
                                              : "{\"blob\": {\"size\": " + image.length + "}}")
           + ", \"textpostText\": " + (postText == null ? null : '"' + postText + '"')
           + ", \"tagIds\": " + (tagIds == null ? null : (tagIds).stream()
                                                                 .map(Objects::toString)
                                                                 .collect(
                                                                     Collectors.joining(
                                                                         ", ", "[", "]")))
           + "}";
  }
}