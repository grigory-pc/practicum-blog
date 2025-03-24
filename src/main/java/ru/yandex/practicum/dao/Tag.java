package ru.yandex.practicum.dao;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс тега.
 */
@Getter
@Setter
@Builder
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
  private Long id;
  private String tagName;

  public Tag(String tagName) {
    this.tagName = tagName;
  }
}