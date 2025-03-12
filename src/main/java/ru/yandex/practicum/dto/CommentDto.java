package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * DTO комментария.
 *
 * @param id - id комментария.
 * @param commentText - текст комментария.
 */
@Builder
public record CommentDto(@JsonProperty(value = "id") Long id,
                         @JsonProperty(value = "text",
                                       required = true) @NotBlank String commentText) {
}