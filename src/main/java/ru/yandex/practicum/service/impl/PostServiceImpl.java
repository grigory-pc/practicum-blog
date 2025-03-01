package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  @Override
  public Page<Post> findAllPosts(Pageable pageable ) {
    return postRepository.findAllPosts();
  }

  @Override
  public void savePost(Post post) {
    postRepository.savePost(post);
  }

  @Override
  public void deletePostById(Long id) {
    postRepository.deletePostById(id);
  }
}