package ru.yandex.practicum.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.dao.Like;
import ru.yandex.practicum.repository.LikeRepository;
import ru.yandex.practicum.service.LikeService;
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
@ContextConfiguration(classes = LikeServiceImpl.class)
class LikeServiceImplTest {
  private static final Long POST_ID = 1L;

  @Autowired
  LikeRepository likeRepository;
  private LikeService likeService;

  @BeforeEach
  void setUp() {
    likeService = new LikeServiceImpl(likeRepository);
  }

  @Test
  void positiveTest_ShouldSaveLike() {
    try {
      doNothing().when(likeRepository)
                 .save(any(Like.class));

      assertDoesNotThrow(
          () -> likeService.saveLike(POST_ID));

      verify(likeRepository, atLeastOnce()).save(any(Like.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldAddLike() {
    try {
      Like like = Data.getLike(POST_ID);

      when(likeRepository.findByPostId(anyLong()))
          .thenReturn(Optional.of(like));

      doNothing().when(likeRepository)
                 .save(any(Like.class));

      assertDoesNotThrow(
          () -> likeService.saveLike(POST_ID));

      verify(likeRepository, atLeastOnce()).findByPostId(anyLong());
      verify(likeRepository, atLeastOnce()).save(any(Like.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }
}