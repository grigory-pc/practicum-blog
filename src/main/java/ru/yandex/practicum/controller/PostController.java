package ru.yandex.practicum.controller;

import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.PagingDto;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

/**
 * Контроллер обрабатывает запросы /posts.
 */
@Slf4j
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;
  private final CommentService commentService;

  /**
   * Обрабатывает GET-запросы на получение списка превью постов для ленты.
   *
   * @param pageNumber - номер страницы.
   * @param pageSize - количество постов на странице.
   * @return страница превью постов.
   */
  @GetMapping
  public String getPosts(@RequestParam(defaultValue = "") String search,
                         @RequestParam(defaultValue = "10") int pageSize,
                         @RequestParam(defaultValue = "1") int pageNumber,
                         Model model) {

    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
    Page<PostDto> posts = postService.findAllPosts(search, pageable);

    model.addAttribute("posts", posts.getContent());
    model.addAttribute("search", search);
    model.addAttribute("paging", new PagingDto(
        pageNumber,
        pageSize,
        posts.hasPrevious(),
        posts.hasNext()
    ));

    return "posts";
  }

  @GetMapping("/add")
  public String showAddPostForm() {
    System.out.println();
    return "add-post";
  }


  /**
   * Обрабатывает GET-запросы на получение поста по id.
   *
   * @param id - id поста.
   * @return страница поста.
   */
  @GetMapping("/{id}")
  public String getPostById(@PathVariable(name = "id") Long id, Model model) {
    log.info("Получен запрос на получение поста для id = {}", id);

    PostDto postDto = postService.getPostById(id);
    log.info("Из базы данных получен пост с id: {}", postDto.getId());

    model.addAttribute("post", postDto);

    return "post";
  }

  /**
   * Сохранение поста.
   */
  @PostMapping
  public String savePost(@RequestPart(value = "title") @NotBlank String title,
                         @RequestPart(value = "image", required = false) MultipartFile image,
                         @RequestPart(value = "text") @NotBlank String text,
                         @RequestPart(value = "tags") String tags) {
    log.info("Получен запрос на добавление поста: title={}, text={}, tags={}", title, text, tags);

    PostDto postDto = PostDto.builder()
                             .title(title)
                             .text(text)
                             .build();

    PostDto savedPost = postService.savePost(postDto, tags, image);
    Long postId = savedPost.getId();
    //    model.addAttribute("post", savedPost);

    log.info("Пост сохранен в базу данных с id={}", postId);

    return "redirect:/posts/" + postId;
  }

  /**
   * Добавление лайка к посту.
   *
   * @param postId - id поста.
   */
  @PostMapping("/{id}/{like}")
  public String addLike(@PathVariable(name = "id") Long postId,
                        @PathVariable(name = "like") boolean like) {
    log.info("Получен запрос на добавление лайка для поста id = {}", postId);

    postService.addLike(postId, like);

    log.info("Для поста id = {} учтен лайк в базе данных", postId);

    return "redirect:/posts/" + postId;
  }

  /**
   * Сохранение комментария.
   *
   * @param postId - id поста.
   */
  @PostMapping("/{postId}/comments")
  public String saveComment(@PathVariable(name = "postId") Long postId,
                            @RequestParam("text") String text) {
    log.info("Получен запрос на добавление комментария: для поста id = {}", postId);

    commentService.saveComment(postId, text);

    log.info("Для поста id = {} добавлен комментарий в базу данных", postId);

    return "redirect:/posts/" + postId;
  }

  /**
   * Удаление поста.
   *
   * @param postId - id поста.
   */
  @DeleteMapping(value = "/{postId}")
  public String deletePost(@PathVariable(name = "postId") Long postId) {
    log.info("Получен запрос на удаление поста id = {}", postId);

    postService.deletePostById(postId);

    log.info("Пост id = {} удален из базы данных", postId);

    return "redirect:/posts/";
  }

  /**
   * Удаление комментария.
   *
   * @param postId - id поста.
   * @param commentId - id комментария.
   */
  @DeleteMapping(value = "/{postId}/comments/{commentId}")
  public String deleteComment(@PathVariable(name = "postId") Long postId,
                              @PathVariable(name = "commentId") Long commentId) {
    log.info("Получен запрос на удаление комментария для id = {}", commentId);

    commentService.deleteCommentById(commentId);

    log.info("Комментарий id = {} удален из базы данных", commentId);

    return "redirect:/posts/" + postId;
  }
}