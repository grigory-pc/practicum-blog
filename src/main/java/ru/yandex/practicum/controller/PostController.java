package ru.yandex.practicum.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.service.PostService;

/**
 * Контроллер обрабатывает запросы /posts.
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
  public static final String REDIRECT_POST = "redirect:/post";
  private final PostService postService;

  /**
   * Обрабатывает GET-запросы на получение списка превью постов для ленты.
   *
   * @param from - номер страницы.
   * @param size - количество постов на странице.
   * @return список превью постов.
   */
  @GetMapping
  public List<PostPreviewDto> getPosts(@RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {

    return postService.findAllPosts(from, size);
  }

  /**
   * Обрабатывает GET-запросы на получение поста по id.
   *
   * @param id - id поста.
   * @return объект поста.
   */
  @GetMapping("/{id}")
  public PostFullDto getPostById(@PathVariable(name = "id") Long id) {

    return postService.getPostById(id);
  }

  /**
   * Сохранение поста.
   *
   * @param post - данные поста.
   * @return возврат на страницу post.html, чтобы она перезагрузилась.
   */
  @PostMapping
  public String savePost(@RequestBody PostSaveDto post) {
    postService.savePost(post);

    return REDIRECT_POST;
  }

  /**
   * Обновление поста.
   *
   * @param id - id поста.
   * @param post - данные поста.
   * @return возврат на страницу post.html, чтобы она перезагрузилась.
   */
  @PatchMapping("/{id}")
  public String updatePost(@PathVariable(name = "id") Long id, @RequestBody PostSaveDto post) {
    postService.updatePost(id, post);

    return REDIRECT_POST;
  }

  /**
   * Добавление лайка к посту.
   *
   * @param id - id поста.
   * @return возврат на страницу post.html, чтобы она перезагрузилась.
   */
  @PostMapping("/{id}/like")
  public String addLike(@PathVariable(name = "id") Long id) {
    postService.addLike(id);

    return REDIRECT_POST;
  }

  /**
   * Сохранение комментария.
   *
   * @param id - id поста.
   * @return возврат на страницу post.html, чтобы она перезагрузилась.
   */
  @PostMapping("/{id}/comment")
  public String saveComment(@PathVariable(name = "id") Long id,
                            @RequestBody CommentDto comment) {
    postService.saveComment(id, comment);

    return REDIRECT_POST;
  }

  /**
   * Обновление комментария.
   *
   * @param id - id поста.
   * @param commentId - id комментария.
   * @param comment - данные комментария.
   * @return возврат на страницу post.html, чтобы она перезагрузилась.
   */
  @PatchMapping("/{id}/comment/{comment_id}")
  public String updComment(@PathVariable(name = "id") Long id,
                           @PathVariable(name = "comment_id") Long commentId,
                           @RequestBody CommentDto comment) {
    postService.updateComment(id, commentId, comment);

    return REDIRECT_POST;
  }

  /**
   * Удаление поста.
   *
   * @param id - id поста.
   * @return возврат на страницу post.html, чтобы она перезагрузилась.
   */
  @PostMapping(value = "/{id}", params = "_method=delete")
  public String deletePost(@PathVariable(name = "id") Long id) {
    postService.deletePostById(id);

    return REDIRECT_POST;
  }

  /**
   * Удаление комментария.
   *
   * @param commentId - id комментария.
   * @return возврат на страницу post.html, чтобы она перезагрузилась.
   */
  @PostMapping(value = "/comment/{commentId}", params = "_method=delete")
  public String deleteComment(@PathVariable(name = "commentId") Long commentId) {
    postService.deleteCommentById(commentId);

    return REDIRECT_POST;
  }

  /**
   * Обрабатывает GET-запросы на получение списка всех тегов.
   *
   * @return список тегов.
   */
  @GetMapping("/tags")
  public List<TagDto> getTags() {

    return postService.findAllTags();
  }
}