package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.exceptions.NotFoundException;

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
  Page<PostPreviewDto> findAllPosts(int from, int size);

  /**
   * Получение поста по id.
   *
   * @param id - id поста.
   * @return объект поста.
   * @throws NotFoundException - исключение в случае, если в базе данных не найдена запись.
   */
  PostFullDto getPostById(Long id) throws NotFoundException;

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
   * @throws NotFoundException - исключение в случае, если в базе данных не найдена запись.
   */
  void updatePost(Long id, PostSaveDto post) throws NotFoundException;

  /**
   * Удаление поста по id.
   *
   * @param id - id поста.
   */
  void deletePostById(Long id);
}