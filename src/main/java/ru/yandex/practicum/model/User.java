package ru.yandex.practicum.model;

import lombok.*;

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
