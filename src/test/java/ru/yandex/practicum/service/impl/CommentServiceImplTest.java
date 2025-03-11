package ru.yandex.practicum.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class CommentServiceImplTest {
  private static final Long ID = 1L;
  @Mock
  private PostRepository postRepository;
  @Mock
  private CommentRepository commentRepository;
  @Mock
  private CommentMapper commentMapper;
  private Data data;

  private CommentService commentService;


  @BeforeEach
  void setUp() {
    commentService = new CommentServiceImpl(postRepository, commentRepository, commentMapper);
  }


  @Test
  void PositiveTest_ShouldSaveComment() {
    try {
      Post post = data.getPost();
      CommentDto commentDto = data.getCommentDto(null);

      when(postRepository.findById(anyLong()))
          .thenReturn(Optional.of(post));

      doNothing().when(commentRepository)
                 .save(any(Comment.class));

      assertDoesNotThrow(
          () -> commentService.saveComment(ID, commentDto));
      verify(postRepository, atLeastOnce()).findById(anyLong());
      verify(commentRepository, atLeastOnce()).save(any(Comment.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void updateComment() {
  }

  @Test
  void deleteCommentById() {
  }
}