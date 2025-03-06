package ru.yandex.practicum.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostTagRepository postTagRepository;
  private final PostMapper postMapper;

  @Override
  public List<PostPreviewDto> findAllPosts(int from, int size) {
    Pageable pageable = PageRequest.of(from, size);

    return postMapper.toFullDto(postRepository.findAllPosts(pageable));
  }

  @Override
  public PostFullDto getPostById(Long id) {
    Optional<Post> existingPost = postRepository.findById(id);
    if (existingPost.isPresent()) {
      return postMapper.toFullDto(existingPost.get());
    } else {
      throw new RuntimeException();
    }
  }

  @Override
  public void savePost(PostSaveDto postSaveDto) {

    Post post = postRepository.save(postMapper.toPost(postSaveDto));


    for (Long tagId : postSaveDto.tagIds()) {
      postTagRepository.save(new PostTag(post.getId(), tagId));
    }
  }

  @Override
  public void updatePost(Long id, PostSaveDto postSaveDto) {
    Optional<Post> existingPost = postRepository.findById(id);
    if (existingPost.isPresent()) {
      Post updatedPost = getUpdatedPost(existingPost.get(), postSaveDto);
      postRepository.save(updatedPost);

      for (Long tagId : postSaveDto.tagIds()) {
        postTagRepository.save(new PostTag(id, tagId));
      }
    }
  }

  @Override
  public void addLike(Long id) {

  }

  @Override
  public void deletePostById(Long id) {
    postRepository.deleteById(id);
  }

  private Post getUpdatedPost(Post postForUpdate, PostSaveDto newPostSaveDto) {
    postForUpdate.setTitle(newPostSaveDto.title());
    postForUpdate.setText(newPostSaveDto.postText());
    postForUpdate.setImage(newPostSaveDto.image());

    return postForUpdate;
  }
}