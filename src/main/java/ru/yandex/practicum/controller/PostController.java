package ru.yandex.practicum.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

/**
 * Контроллер обрабатывает запросы /posts.
 */
@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;
  private final CommentService commentService;
  private final TagService tagService;
  private final LikeService likeService;

  /**
   * Обрабатывает GET-запросы на получение списка превью постов для ленты.
   *
   * @param from - номер страницы.
   * @param size - количество постов на странице.
   * @return список превью постов.
   */
  @GetMapping
  public Page<PostPreviewDto> getPosts(@RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
    log.info("Получен запрос на получение preview постов");

    Page<PostPreviewDto> postsPreviewPage = postService.findAllPosts(from, size);

    log.info("Получен список preview постов размером: {}", postsPreviewPage.getTotalElements());

    return postsPreviewPage;
  }

  /**
   * Обрабатывает GET-запросы на получение поста по id.
   *
   * @param id - id поста.
   * @return объект поста.
   */
  @GetMapping("/{id}")
  public PostFullDto getPostById(@PathVariable(name = "id") Long id) {
    log.info("Получен запрос на получение поста для id = {}", id);

    PostFullDto postFullDto = postService.getPostById(id);

    log.info("Из базы данных получен пост: {}", postFullDto);

    return postFullDto;
  }

  /**
   * Сохранение поста.
   *
   * @param post - данные поста.
   */
  @PostMapping
  public void savePost(@RequestBody PostSaveDto post) {
    log.info("Получен запрос на добавление поста: {}", post);

    postService.savePost(post);
    log.info("Пост сохранен в базу данных");
  }

  /**
   * Обновление поста.
   *
   * @param id   - id поста.
   * @param post - данные поста.
   * @return возврат на страницу post.html, чтобы она перезагрузилась.
   */
  @PatchMapping("/{id}")
  public void updatePost(@PathVariable(name = "id") Long id, @RequestBody PostSaveDto post) {
    log.info("Получен запрос на обновление поста: {} для id = {}", post, id);

    postService.updatePost(id, post);

    log.info("Пост id = {} обновлен в базе данных", id);
  }

  /**
   * Добавление лайка к посту.
   *
   * @param id - id поста.
   */
  @PostMapping("/{id}/like")
  public void addLike(@PathVariable(name = "id") Long id) {
    log.info("Получен запрос на добавление лайка для поста id = {}", id);

    likeService.addLike(id);

    log.info("Для поста id = {} добавлен лайк в базу данных", id);
  }

  /**
   * Сохранение комментария.
   *
   * @param id - id поста.
   */
  @PostMapping("/{id}/comment")
  public void saveComment(@PathVariable(name = "id") Long id,
                          @RequestBody CommentDto comment) {
    log.info("Получен запрос на добавление комментария: {} для поста id = {}", comment, id);

    commentService.saveComment(id, comment);

    log.info("Для поста id = {} добавлен комментарий в базу данных", id);
  }

  /**
   * Обновление комментария.
   *
   * @param id        - id поста.
   * @param commentId - id комментария.
   * @param comment   - данные комментария.
   */
  @PatchMapping("/{id}/comment/{comment_id}")
  public void updateComment(@PathVariable(name = "id") Long id,
                              @PathVariable(name = "comment_id") Long commentId,
                              @RequestBody CommentDto comment) {
    log.info("Получен запрос на обновление комментария: {} для поста id = {}", comment, id);

    commentService.updateComment(id, commentId, comment);

    log.info("Для поста id = {} обновлен комментарий в базе данных", id);
  }

  /**
   * Удаление поста.
   *
   * @param id - id поста.
   */
  @PostMapping(value = "/{id}", params = "_method=delete")
  public void deletePost(@PathVariable(name = "id") Long id) {
    log.info("Получен запрос на удаление поста id = {}", id);

    postService.deletePostById(id);

    log.info("Пост id = {} удален из базы данных", id);
  }

  /**
   * Удаление комментария.
   *
   * @param commentId - id комментария.
   */
  @PostMapping(value = "/comment/{commentId}", params = "_method=delete")
  public void deleteComment(@PathVariable(name = "commentId") Long commentId) {
    log.info("Получен запрос на удаление комментария для id = {}", commentId);

    commentService.deleteCommentById(commentId);

    log.info("Комментарий id = {} удален из базы данных", commentId);
  }

  /**
   * Обрабатывает GET-запросы на получение списка всех тегов.
   *
   * @return список тегов.
   */
  @GetMapping("/tags")
  public List<TagDto> getTags() {
    log.info("Получен запрос на получение тегов");

    return tagService.findAllTags();
  }
}