package ru.yandex.practicum.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Like;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.repository.LikeRepository;
import ru.yandex.practicum.service.LikeService;

@Slf4j
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
    Optional<Like> existingLikesOptional = likeRepository.findByPostId(id);

    log.info("Из БД получена запись = {}", existingLikesOptional);

    if (existingLikesOptional.isPresent()) {
      Like existingLikes = existingLikesOptional.get();

      Integer likesCount = existingLikes.getCountLikes();
      likesCount++;

      existingLikes.setCountLikes(likesCount);

      likeRepository.save(existingLikes);
    } else {
      throw new NotFoundException();
    }
  }
}
