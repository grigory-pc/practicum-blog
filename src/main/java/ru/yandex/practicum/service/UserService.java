package ru.yandex.practicum.service;

import ru.yandex.practicum.model.User;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 */
public interface UserService {

  /**
   * Получение списка пользователей.
   *
   * @return список пользователей.
   */
  List<User> findAll();

  /**
   * Сохранение пользователя.
   *
   * @param user - объект пользователя.
   */
  void save(User user);

  /**
   * Удаление пользователя по id.
   * @param id - id пользователя.
   */
  void deleteById(Long id);
}