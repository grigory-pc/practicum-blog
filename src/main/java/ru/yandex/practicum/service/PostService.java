package ru.yandex.practicum.service;

import java.util.List;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;

/**
 * Сервис для работы с постами.
 */
public interface PostService {

  /**
   * Получение всех постов.
   *
   * @param from - с какой страницы
   * @param size - количество записей.
   * @return список постов.
   */
  List<PostPreviewDto> findAllPosts(int from, int size);

  /**
   * Получение поста по id.
   *
   * @param id - id поста.
   * @return объект поста.
   */
  PostFullDto getPostById(Long id);

  /**
   * Сохранение поста.
   *
   * @param post - объект поста.
   */
  void savePost(PostSaveDto post);

  /**
   * Обновление поста.
   *
   * @param id - id поста.
   * @param post - объект поста.
   */
  void updatePost(Long id, PostSaveDto post);

  /**
   * Добавление лайка к посту.
   *
   * @param id - id поста.
   */
  void addLike(Long id);

  /**
   * Добавление комментария.
   *
   * @param postId - id поста.
   */
  void saveComment(Long postId, CommentDto comment);

  /**
   * Обновление комментария.
   *
   * @param id - id поста.
   * @param commentId - id комментария.
   * @param comment - данные комментария.
   */
  void updateComment(Long id, Long commentId, CommentDto comment);

  /**
   * Удаление поста по id.
   *
   * @param id - id поста.
   */
  void deletePostById(Long id);

  /**
   * Удаление комментария по id.
   *
   * @param id - id комментария.
   */
  void deleteCommentById(Long id);

  /**
   * Получение списка всех тегов.
   *
   * @return список тегов.
   */
  List<TagDto> findAllTags();
}