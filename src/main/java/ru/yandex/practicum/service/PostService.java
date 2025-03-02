package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dao.Post;

/**
 * Сервис для работы с постами.
 */
public interface PostService {

    /**
     * Получение списка постов.
     *
     * @param pageable - данные для пагинации.
     * @return список постов.
     */
    Page<Post> findAllPosts(Pageable pageable);

    /**
     * Сохранение поста.
     *
     * @param post - объект поста.
     */
    void savePost(Post post);

    /**
     * Удаление поста по id.
     *
     * @param id - id поста.
     */
    void deletePostById(Long id);
}