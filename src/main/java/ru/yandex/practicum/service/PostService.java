package ru.yandex.practicum.service;

import java.util.List;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;

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
   * Удаление поста по id.
   *
   * @param id - id поста.
   */
  void deletePostById(Long id);
}