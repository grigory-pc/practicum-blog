package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.utils.OffsetBasedPageRequest;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  @Override
  public Page<Post> findAllPosts(int from, int size ) {
    Pageable pageable = OffsetBasedPageRequest.of(from, size);

    return postRepository.findAllPosts(pageable);
  }

  @Override
  public void savePost(Post post) {
    postRepository.save(post);
  }

  @Override
  public void deletePostById(Long id) {
    postRepository.deletePostById(id);
  }
}