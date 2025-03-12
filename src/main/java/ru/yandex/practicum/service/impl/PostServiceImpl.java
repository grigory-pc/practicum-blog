package ru.yandex.practicum.service.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.service.PostService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostTagRepository postTagRepository;
  private final TagRepository tagRepository;
  private final PostMapper postMapper;
  private final LikeService likeService;

  @Override
  public Page<PostPreviewDto> findAllPosts(int from, int size) {
    Pageable pageable = PageRequest.of(from, size);

    Page<Post> posts = postRepository.findAllPosts(pageable);

    Map<Long, Set<Tag>> postTagMap = getPostTagsForSetPosts(posts);

    posts.getContent().forEach(
        post -> post.setTags(postTagMap.getOrDefault(post.getId(), Collections.emptySet())));

    return postMapper.toDtoPage(posts);
  }

  @Override
  public PostFullDto getPostById(Long id) throws NotFoundException {
    Post post = postRepository.findById(id)
                              .orElseThrow(NotFoundException::new);

    post.setTags(postTagRepository.findAllByPostId(post.getId())
                                  .stream()
                                  .flatMap(postTag -> tagRepository.findById(postTag.getTagId())
                                                                   .stream())
                                  .collect(Collectors.toSet()));

    return postMapper.toFullDto(post);
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
    Post existingPost = postRepository.findById(id)
                                      .orElseThrow(NotFoundException::new);

    log.info("Из БД получена запись = {}", existingPost);

    Post updatedPost = getUpdatedPost(existingPost, postSaveDto);
    postRepository.save(updatedPost);

    postTagRepository.deleteAllByPostId(id);
    postSaveDto.tagIds().forEach(tagId -> postTagRepository.save(new PostTag(id, tagId)));
  }

  @Override
  public void deletePostById(Long id) {
    postRepository.deleteById(id);

    postTagRepository.deleteAllByPostId(id);
  }

  private Map<Long, Set<Tag>> getPostTagsForSetPosts(Page<Post> posts) {
    Set<Long> postIds = posts.getContent().stream()
                             .map(Post::getId)
                             .collect(Collectors.toSet());

    Set<PostTag> postTags = postTagRepository.findAllByPostIdIn(postIds);

    Set<Long> tagIds = postTags.stream()
                               .map(PostTag::getTagId)
                               .collect(Collectors.toSet());

    Map<Long, Tag> tagsMap = tagRepository.findAllByIdIn(tagIds)
                                          .stream()
                                          .collect(
                                              Collectors.toMap(Tag::getId, Function.identity()));

    return postTags.stream()
                   .collect(Collectors.groupingBy(PostTag::getPostId, Collectors.mapping(
                       postTag -> tagsMap.get(postTag.getTagId()), Collectors.toSet())
                   ));
  }

  private Post getUpdatedPost(Post postForUpdate, PostSaveDto newPostSaveDto) {
    postForUpdate.setTitle(newPostSaveDto.title());
    postForUpdate.setText(newPostSaveDto.postText());
    postForUpdate.setImage(newPostSaveDto.image());

    return postForUpdate;
  }
}