package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;

/**
 * DTO поста со всей информацией для отображения.
 *
 * @param id - id поста.
 * @param title -названия поста.
 * @param image - картинка.
 * @param postText - текст поста.
 * @param tags - теги поста.
 * @param comments - комментарии.
 */
@Builder
public record PostFullDto(@JsonProperty(value = "id") Long id,
                          @JsonProperty(value = "title",
                                        required = true) @NotBlank String title,
                          @JsonProperty(value = "image") byte[] image,
                          @JsonProperty(value = "post_text",
                                        required = true) @NotBlank String postText,
                          @JsonProperty(value = "tags") Set<TagDto> tags,
                          @JsonProperty(value = "comments",
                                        required = true) List<CommentDto> comments) {
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PostFullDto that = (PostFullDto) object;
    return Objects.equals(id, that.id) && Objects.equals(title, that.title)
           && Arrays.equals(image, that.image) && Objects.equals(postText,
                                                                 that.postText)
           && Objects.equals(tags, that.tags) && Objects.equals(comments,
                                                                that.comments);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, title, postText, tags, comments);
    result = 31 * result + Arrays.hashCode(image);
    return result;
  }

  @Override
  public String toString() {
    return "PostFullDto{" +
           "id=" + id +
           ", title='" + title + '\'' +
           ", image=" + Arrays.toString(image) +
           ", postText='" + postText + '\'' +
           ", tags=" + tags +
           ", comments=" + comments +
           '}';
  }
}