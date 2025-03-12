package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO тега.
 *
 * @param id - id тега.
 * @param tagName - текст тега.
 */
public record TagDto(@JsonProperty(value = "id") Long id,
                     @JsonProperty(value = "tag") @NotBlank String tagName) {
}