package ru.yandex.practicum.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

/**
 * Контроллер обрабатывает запросы /posts.
 */
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * Обрабатывает GET-запросы на получение списка постов.
     *
     * @param page - номер страницы.
     * @param size - количество постов на странице.
     * @return список постов.
     */
    @GetMapping
    public ResponseEntity<List<Post>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute Post post) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postService.findAllPosts(pageable);

        return ResponseEntity.ok(postPage.getContent());
    }

    /**
     * Сохранение поста.
     *
     * @param post - данные поста.
     * @return возврат на страницу posts.html, чтобы она перезагрузилась.
     */
    @PostMapping
    public String savePost(@ModelAttribute Post post) {
        postService.savePost(post);

        return "redirect:/posts";
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String deletePost(@PathVariable(name = "id") Long id) {
        postService.deletePostById(id);

        return "redirect:/posts";
    }
}