package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Like;
import ru.yandex.practicum.repository.Impl.JdbcLikeRepositoryImpl;
import ru.yandex.practicum.service.LikeService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
  private static final Integer DEFAULT_COUNT_LIKES = 0;

  private final JdbcLikeRepositoryImpl likeRepository;

  @Override
  public void saveLike(Long id) {
    Like newLike = new Like();
    newLike.setPostId(id);
    newLike.setCountLikes(DEFAULT_COUNT_LIKES);

    likeRepository.save(newLike);
  }

  @Override
  public void addLike(Long postId) {
      likeRepository.incrementLikes(postId);
  }
}