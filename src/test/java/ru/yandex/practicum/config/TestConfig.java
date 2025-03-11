package ru.yandex.practicum.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.LikeRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

@Configuration
@ComponentScan("ru.yandex.practicum")
public class TestConfig {
  @Bean
  @Primary
  public CommentService mockCommentService () {
    return Mockito.mock(CommentService.class);
  }

  @Bean
  @Primary
  public PostService mockPostService () {
    return Mockito.mock(PostService.class);
  }

  @Bean
  @Primary
  public LikeService mockLikeService () {
    return Mockito.mock(LikeService.class);
  }

  @Bean
  @Primary
  public TagService mockTagService () {
    return Mockito.mock(TagService.class);
  }

  @Bean
  @Primary
  public CommentRepository mockCommentRepository () {
    return Mockito.mock(CommentRepository.class);
  }
  @Bean
  @Primary
  public LikeRepository mockLikeRepository () {
    return Mockito.mock(LikeRepository.class);
  }

  @Bean
  @Primary
  public PostRepository mockPostRepository () {
    return Mockito.mock(PostRepository.class);
  }

  @Bean
  @Primary
  public PostTagRepository mockPostTagRepository() {
    return Mockito.mock(PostTagRepository.class);
  }

  @Bean
  @Primary
  public TagRepository mockTagRepository() {
    return Mockito.mock(TagRepository.class);
  }

  @Bean
  @Primary
  public CommentMapper mockCommentMapper() {
    return Mockito.mock(CommentMapper.class);
  }

  @Bean
  @Primary
  public PostMapper mockPostMapper () {
    return Mockito.mock(PostMapper.class);
  }

  @Bean
  @Primary
  public TagMapper mockTagMapper () {
    return Mockito.mock(TagMapper.class);
  }
}
