package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс пользователя.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private Long id;
  private String firstName;
  private String lastName;
  private int age;
  private boolean active;
}
