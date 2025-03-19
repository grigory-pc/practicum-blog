package ru.yandex.practicum.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dao.PostTag;

/**
 * Получение данных из таблицы tags-posts.
 */
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
  /**
   * Поиск всех тегов по  post id.
   *
   * @param postId - post id.
   * @return коллекция связей post id и id тега.
   */
  List<PostTag> findAllByPostId(Long postId);

  /**
   * Удаление всех записей по post id.
   *
   * @param postId - id поста.
   */
  @Transactional
  void deleteAllByPostId(Long postId);
}