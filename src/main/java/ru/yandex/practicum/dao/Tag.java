package ru.yandex.practicum.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс тега.
 */
@Getter
@Setter
@Entity
@Builder
@Table(name = "tags")
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "tag_name", nullable = false)
  private String tagName;

  public Tag(Long id, String tagName) {
    this.id = id;
    this.tagName = tagName;
  }

  public Tag(String tagName) {
    this.tagName = tagName;
  }
}