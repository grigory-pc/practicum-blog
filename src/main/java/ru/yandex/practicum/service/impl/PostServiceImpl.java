package ru.yandex.practicum.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;

  @Override
  public List<PostPreviewDto> findAllPosts(int from, int size ) {
    Pageable pageable = PageRequest.of(from, size);

    return postMapper.toDto(postRepository.findAllPosts(pageable));
  }

  @Override
  public PostFullDto getPostById(Long id) {
    return null;
  }

  @Override
  public void savePost(PostSaveDto post) {
  }

  @Override
  public void updatePost(Long id, PostSaveDto post) {

  }

  @Override
  public void addLike(Long id) {

  }

  @Override
  public void saveComment(Long id, CommentDto comment) {

  }

  @Override
  public void updateComment(Long id, Long commentId, CommentDto comment) {

  }

  @Override
  public void deletePostById(Long id) {
    postRepository.deletePostById(id);
  }

  @Override
  public void deleteCommentById(Long id) {

  }
}