package ru.yandex.practicum.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.service.PostService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostTagRepository postTagRepository;
  private final PostMapper postMapper;
  private final LikeService likeService;

  @Override
  public Page<PostPreviewDto> findAllPosts(int from, int size) {
    Pageable pageable = PageRequest.of(from, size);

    return postMapper.toDtoPage(postRepository.findAllPosts(pageable));
  }

  @Override
  public PostFullDto getPostById(Long id) {

    return postRepository.findById(id)
                         .map(postMapper::toFullDto)
                         .orElseThrow(NotFoundException::new);
  }

  @Override
  public void savePost(PostSaveDto postSaveDto) {
    Post post = postRepository.save(postMapper.toPost(postSaveDto));

    for (Long tagId : postSaveDto.tagIds()) {
      postTagRepository.save(new PostTag(post.getId(), tagId));
    }

    likeService.saveLike(post.getId());
  }

  @Override
  public void updatePost(Long id, PostSaveDto postSaveDto) {
    Optional<Post> existingPost = postRepository.findById(id);

    log.info("Из БД получена запись = {}", existingPost);

    if (existingPost.isPresent()) {
      Post updatedPost = getUpdatedPost(existingPost.get(), postSaveDto);
      postRepository.save(updatedPost);

      for (Long tagId : postSaveDto.tagIds()) {
        postTagRepository.save(new PostTag(id, tagId));
      }
    } else {
      throw new NotFoundException();
    }
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