package ru.yandex.practicum.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

/**
 *Контроллер обрабатывает запросы /users.
 */
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  /**
   * Обрабатывает GET-запросы.
   * Данные передаются в виде атрибута users.
   *
   * @return название шаблона — users.html.
   */
  @GetMapping
  public String users(Model model) {
    List<User> users = service.findAll();

    model.addAttribute("users", users);

    return "users";
  }

  /**
   * Сохранение пользователей.
   *
   * @param user - данные пользователя.
   * @return возврат на страницу users.html, чтобы она перезагрузилась.
   */
  @PostMapping
  public String save(@ModelAttribute User user) {
    service.save(user);

    return "redirect:/users";
  }

  @PostMapping(value = "/{id}", params = "_method=delete")
  public String delete(@PathVariable(name = "id") Long id) {
    service.deleteById(id);

    return "redirect:/users";
  }
}