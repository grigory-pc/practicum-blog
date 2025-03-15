package ru.yandex.practicum.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.config.DataTestSource;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.CommentDto;
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
@Import(DataTestSource.class)
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
  void positiveTest_shouldSaveComment() {
    try {
      Post post = Data.getPost();
      Comment comment = Data.getComment(ID);
      CommentDto commentDto = Data.getCommentDto(null);

      doReturn(comment)
          .when(commentMapper).toComment(any(CommentDto.class));

      doReturn(Optional.of(post))
          .when(postRepository).findById(anyLong());

      doReturn(comment)
          .when(commentRepository).save(any(Comment.class));

      assertDoesNotThrow(
          () -> commentService.saveComment(ID, commentDto));

      verify(commentMapper, atLeastOnce()).toComment(any(CommentDto.class));
      verify(postRepository, atLeastOnce()).findById(anyLong());
      verify(commentRepository, atLeastOnce()).save(any(Comment.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_shouldUpdateComment() {
    try {
      Comment comment = Data.getComment(ID);
      CommentDto commentDto = Data.getCommentDto(null);

      doReturn(Optional.of(comment))
          .when(commentRepository).findById(anyLong());

      doReturn(comment)
          .when(commentRepository).save(any(Comment.class));

      assertDoesNotThrow(
          () -> commentService.updateComment(ID, ID, commentDto));

      verify(commentRepository, atLeastOnce()).findById(anyLong());
      verify(commentRepository, atLeastOnce()).save(any(Comment.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
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