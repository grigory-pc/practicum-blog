package ru.yandex.practicum.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.config.DataSourceTestConfig;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@Import(DataSourceTestConfig.class)
@ContextConfiguration(classes = {CommentServiceImpl.class})
class CommentServiceImplTest {
  private static final Long ID = 1L;
  @MockitoBean
  private PostRepository postRepository;
  @MockitoBean
  private CommentRepository commentRepository;
  @MockitoBean
  private CommentMapper commentMapper;
  @Autowired
  private CommentService commentService;

  @Test
  @DisplayName("Позитивный тест - сохранение комментария")
  void positiveTest_shouldSaveComment() {
    try {
      String text = "text";
      Post post = Data.getPost();
      Comment comment = Data.getComment(ID);

      doReturn(Optional.of(post))
          .when(postRepository).findById(anyLong());

      doReturn(comment)
          .when(commentRepository).save(any(Comment.class));

      assertDoesNotThrow(
          () -> commentService.saveComment(ID, text));

      verify(postRepository, atLeastOnce()).findById(anyLong());
      verify(commentRepository, atLeastOnce()).save(any(Comment.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  @DisplayName("Позитивный тест - обновление комментария")
  void positiveTest_shouldUpdateComment() {
    try {
      String text = "text";
      Post post = Data.getPost();
      Comment comment = Data.getComment(ID);

      doReturn(Optional.of(post))
          .when(postRepository).findById(anyLong());

      doReturn(comment)
          .when(commentRepository).save(any(Comment.class));

      assertDoesNotThrow(
          () -> commentService.updateComment(ID, ID, text));

      verify(postRepository, atLeastOnce()).findById(anyLong());
      verify(commentRepository, atLeastOnce()).save(any(Comment.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  @DisplayName("Позитивный тест - удаление комментария")
  void positiveTest_shouldDeleteCommentById() {
    try {
      doNothing().when(commentRepository)
                 .deleteById(anyLong());

      assertDoesNotThrow(
          () -> commentService.deleteCommentById(ID));

      verify(commentRepository, atLeastOnce()).deleteById(anyLong());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }
}