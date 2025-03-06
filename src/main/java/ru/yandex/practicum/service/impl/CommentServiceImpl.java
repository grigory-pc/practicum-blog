package ru.yandex.practicum.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

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
  public void deleteCommentById(Long id) {

  }

  private Comment getUpdatedComment(Comment existingComment, CommentDto commentDto) {
    existingComment.setText(commentDto.commentText());

    return existingComment;
  }
}