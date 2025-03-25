package ru.yandex.practicum.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.dao.Post;

/**
 * Получение данных из таблицы Posts.
 */
public interface PostRepository {

  /**
   * Поиск поста по id.
   *
   * @param id - id поста.
   * @return найденный пост.
   */
  Optional<Post> findById(Long id);

  /**
   * Сохранение поста.
   *
   * @param post - объект поста.
   * @return id сохраненного поста.
   */
  Long save(Post post);

  /**
   * Обновление поста.
   *
   * @param post - объект поста.
   */
  void update(Post post);

  /**
   * Получение всех постов.
   *
   * @param pageNumber - с какой страницы
   * @param pageSize - количество записей.
   * @return список постов.
   */
  Page<Post> findAll(int pageNumber, int pageSize);

  /**
   * Получение всех постов без пагинации.
   *
   * @return список постов.
   */
  List<Post> findAll();

  /**
   * Увеличение количества лайков на 1.
   *
   * @param postId - id поста.
   */
  void increaseLikesCount(@Param("postId") Long postId);

  /**
   * Уменьшение количества лайков на 1.
   *
   * @param postId - id поста.
   */
  void decreaseLikesCount(@Param("postId") Long postId);

  /**
   * Поиск постов на базе тега.
   *
   * @param search   - строка с тегами.
   * @param from - с какой страницы
   * @param size - количество записей.
   * @return коллекция постов с параметрами пагинации.
   */
  Page<Post> findByTags_NameContainingIgnoreCase(String search, int from, int size);

  /**
   * Удаление поста.
   *
   * @param id - id поста.
   */
  void deletePostById(Long id);
}