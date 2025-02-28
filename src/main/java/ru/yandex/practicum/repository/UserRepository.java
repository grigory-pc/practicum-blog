package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.User;
import java.util.List;

/**
 * Получение данных из таблицы Users.
 */
public interface UserRepository {

  /**
   * Получение всех пользователей.
   *
   * @return список пользователей.
   */
  List<User> findAll();

  /**
   * Сохранение пользователя в БД.
   *
   * @param user - объект пользователя.
   */
  void save(User user);

  /**
   * Удаление пользователя из БД по id.
   * @param id - id пользователя.
   */
  void deleteById(Long id);
}
