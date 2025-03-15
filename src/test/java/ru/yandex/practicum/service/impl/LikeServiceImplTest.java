package ru.yandex.practicum.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dao.Like;
import ru.yandex.practicum.repository.LikeRepository;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LikeServiceImpl.class)
class LikeServiceImplTest {
  private static final Long POST_ID = 1L;

  @MockitoBean
  LikeRepository likeRepository;
  @Autowired
  private LikeService likeService;

  @Test
  void positiveTest_shouldSaveLike() {
    try {
      Like like = Data.getLike(POST_ID);

      doReturn(like)
          .when(likeRepository).save(any(Like.class));

      assertDoesNotThrow(
          () -> likeService.saveLike(POST_ID));

      verify(likeRepository, atLeastOnce()).save(any(Like.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_shouldAddLike() {
    try {
      Like like = Data.getLike(POST_ID);

      doReturn(Optional.of(like))
          .when(likeRepository).findByPostId(anyLong());
      doReturn(like)
          .when(likeRepository).save(any(Like.class));

      assertDoesNotThrow(
          () -> likeService.addLike(POST_ID));

      verify(likeRepository, atLeastOnce()).findByPostId(anyLong());
      verify(likeRepository, atLeastOnce()).save(any(Like.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }
}