package ru.yandex.practicum.service.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.CommentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  @Override
  @Transactional
  public void saveComment(Long postId, String text) throws NotFoundException {
    Comment comment = Comment.builder()
                             .commentText(text)
                             .build();

    Optional<Post> post = postRepository.findById(postId);

    log.info("Из БД получена запись = {}", post);

    if (post.isPresent()) {
      comment.setPost(post.get());

      commentRepository.save(comment);
    } else {
      throw new NotFoundException();
    }
  }

  @Override
  public void deleteCommentById(Long id) {
    commentRepository.deleteById(id);
  }
}