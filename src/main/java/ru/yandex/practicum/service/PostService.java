package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.exceptions.NotFoundException;

/**
 * Сервис для работы с постами.
 */
public interface PostService {

  /**
   * Получение всех постов.
   *
   * @param search     - строка поиска
   * @param pageNumber - с какой страницы
   * @param pageSize   - количество записей.
   * @return список постов.
   */
  Page<PostDto> findAllPosts(String search, int pageNumber, int pageSize);

  /**
   * Получение поста по id.
   *
   * @param id - id поста.
   * @return объект поста.
   * @throws NotFoundException - исключение в случае, если в базе данных не найдена запись.
   */
  PostDto getPostById(Long id) throws NotFoundException;

  /**
   * Сохранение поста.
   *
   * @param postDto - объект поста.
   * @param tags    - теги.
   * @param image   - картинка.
   * @return сохраненный объект поста.
   */
  PostDto savePost(PostDto postDto, String tags, MultipartFile image);


  /**
   * Удаление поста по id.
   *
   * @param id - id поста.
   */
  void deletePostById(Long id);

  /**
   * Добавление лайка к посту.
   *
   * @param postId - id поста.
   * @param like   - true или false. Увеличить или уменьшить лайк.
   * @throws NotFoundException - исключение в случае, если в базе данных не найдена запись.
   */
  void addLike(Long postId, boolean like) throws NotFoundException;

  /**
   * Получение изображения.
   *
   * @param postId - id поста.
   * @return байтовый массив изображения.
   */
  byte[] getPostImage(Long postId);
}