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
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

/**
 *Контроллер обрабатывает запросы /posts.
 */
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
  private final PostService service;

  /**
   * Обрабатывает GET-запросы.
   * Данные передаются в виде атрибута posts.
   *
   * @return название шаблона — posts.html.
   */
  @GetMapping
  public String getPosts(Model model) {
    List<Post> posts = service.findPostAll();

    model.addAttribute("posts", posts);

    return "posts";
  }

  /**
   * Сохранение поста.
   *
   * @param post - данные поста.
   * @return возврат на страницу posts.html, чтобы она перезагрузилась.
   */
  @PostMapping
  public String savePost(@ModelAttribute Post post) {
    service.savePost(post);

    return "redirect:/posts";
  }

  @PostMapping(value = "/{id}", params = "_method=delete")
  public String deletePost(@PathVariable(name = "id") Long id) {
    service.deletePostById(id);

    return "redirect:/posts";
  }
}