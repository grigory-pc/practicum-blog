package ru.yandex.practicum.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final TagRepository tagRepository;
  private final PostTagRepository postTagRepository;
  private final PostMapper postMapper;
  private final TagMapper tagMapper;

  @Override
  public List<PostPreviewDto> findAllPosts(int from, int size) {
    Pageable pageable = PageRequest.of(from, size);

    return postMapper.toDto(postRepository.findAllPosts(pageable));
  }

  @Override
  public PostFullDto getPostById(Long id) {

    return postMapper.toDto(postRepository.findById(id));
  }

  @Override
  public void savePost(PostSaveDto postSaveDto) {
    Post post = postRepository.save(postMapper.toPost(postSaveDto));

    List<Tag> tags = tagMapper.toTag(postSaveDto.tags());

    for (Tag tag : tags) {
      Tag savedTag = tagRepository.save(tag);
      postTagRepository.save(new PostTag(post.getId(), savedTag.getId()));
    }
  }

  @Override
  public void updatePost(Long id, PostSaveDto postSaveDto) {
    Post existingPost = postRepository.findById(id);

    Post newPost = getNewPost(existingPost, postSaveDto);

    Post post = postRepository.save(newPost);

    List<Tag> tags = tagMapper.toTag(postSaveDto.tags());

    for (Tag tag : tags) {
      Tag savedTag = tagRepository.save(tag);
      postTagRepository.save(new PostTag(post.getId(), savedTag.getId()));
    }
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

  private Post getNewPost(Post existingPost, PostSaveDto postSaveDto) {
    Post newPost = postMapper.toPost(postSaveDto);
    if (existingPost != null) {
      existingPost.setTitle(newPost.getTitle());
      existingPost.setText(newPost.getText());
      existingPost.setImage(newPost.getImage());
      return existingPost;
    }
    return newPost;
  }
}