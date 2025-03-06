package ru.yandex.practicum.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostTagRepository postTagRepository;
  private final TagRepository tagRepository;
  private final CommentRepository commentRepository;
  private final PostMapper postMapper;
  private final TagMapper tagMapper;
  private final CommentMapper commentMapper;

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
  public void saveComment(Long postId, CommentDto commentDto) {
    Comment newComment = commentMapper.toComment(commentDto);

    Optional<Post> post = postRepository.findById(postId);
    if (post.isPresent()) {
      newComment.setPost(post.get());
      commentRepository.save(newComment);
    }
  }

  @Override
  public void updateComment(Long id, Long commentId, CommentDto commentDto) {
    Optional<Comment> existingComment = commentRepository.findById(commentId);
    if (existingComment.isPresent()) {
      Comment updatedComment = getUpdatedComment(existingComment.get(), commentDto);

      commentRepository.save(updatedComment);
    }
  }

  @Override
  public void deletePostById(Long id) {
    postRepository.deletePostById(id);
  }

  @Override
  public void deleteCommentById(Long id) {

  }

  @Override
  public List<TagDto> findAllTags() {
    return tagMapper.toDto(tagRepository.findAll());
  }

  private Post getUpdatedPost(Post postForUpdate, PostSaveDto newPostSaveDto) {
    postForUpdate.setTitle(newPostSaveDto.title());
    postForUpdate.setText(newPostSaveDto.postText());
    postForUpdate.setImage(newPostSaveDto.image());

    return postForUpdate;
  }

  private Comment getUpdatedComment(Comment existingComment, CommentDto commentDto) {
    existingComment.setText(commentDto.commentText());

    return existingComment;
  }
}