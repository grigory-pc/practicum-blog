package ru.yandex.practicum.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = CommentServiceImpl.class)
class CommentServiceImplTest {
  private static final Long ID = 1L;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private CommentMapper commentMapper;
  private CommentService commentService;


  @BeforeEach
  void setUp() {
    commentService = new CommentServiceImpl(postRepository, commentRepository, commentMapper);
  }

  @Test
  void positiveTest_ShouldSaveComment() {
    try {
      Post post = Data.getPost();
      Comment comment = Data.getComment(ID);
      CommentDto commentDto = Data.getCommentDto(null);

      when(commentMapper.toComment(any(CommentDto.class)))
          .thenReturn(comment);

      when(postRepository.findById(anyLong()))
          .thenReturn(Optional.of(post));

      doNothing().when(commentRepository)
                 .save(any(Comment.class));

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
  void positiveTest_ShouldUpdateComment() {
    try {
      Comment comment = Data.getComment(ID);
      CommentDto commentDto = Data.getCommentDto(null);

      when(commentRepository.findById(anyLong()))
          .thenReturn(Optional.of(comment));

      doNothing().when(commentRepository)
                 .save(any(Comment.class));

      assertDoesNotThrow(
          () -> commentService.updateComment(ID, ID, commentDto));

      verify(commentRepository, atLeastOnce()).findById(anyLong());
      verify(commentRepository, atLeastOnce()).save(any(Comment.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldDeleteCommentById() {
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