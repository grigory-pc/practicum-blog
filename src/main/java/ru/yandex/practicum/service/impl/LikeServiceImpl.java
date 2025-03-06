package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Like;
import ru.yandex.practicum.repository.LikeRepository;
import ru.yandex.practicum.service.LikeService;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeRepository likeRepository;

  @Override
  public void saveLike(Long id) {
    Like newLike = new Like();
    newLike.setPostId(id);

    likeRepository.save(newLike);
  }

  @Override
  public void addLike(Long id) {
    Like existingLikes = likeRepository.findByPostId(id);

    Integer likesCount = existingLikes.getLikesCount();
    likesCount++;

    existingLikes.setLikesCount(likesCount);

    likeRepository.save(existingLikes);
  }
}
