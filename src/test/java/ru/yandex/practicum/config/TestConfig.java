package ru.yandex.practicum.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.repository.JdbcCommentRepository;
import ru.yandex.practicum.repository.JdbcLikeRepository;
import ru.yandex.practicum.repository.JdbcPostRepository;
import ru.yandex.practicum.repository.JdbcPostTagRepository;
import ru.yandex.practicum.repository.JdbcTagRepository;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

@Configuration
@ComponentScan("ru.yandex.practicum")
public class TestConfig {

  @Bean
  @Primary
  public PostService mockPostService () {
    return Mockito.mock(PostService.class);
  }

  @Bean
  @Primary
  public CommentService mockCommentService () {
    return Mockito.mock(CommentService.class);
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
  public JdbcCommentRepository mockCommentRepository () {
    return Mockito.mock(JdbcCommentRepository.class);
  }
  @Bean
  @Primary
  public JdbcLikeRepository mockLikeRepository () {
    return Mockito.mock(JdbcLikeRepository.class);
  }

  @Bean
  @Primary
  public JdbcPostRepository mockPostRepository () {
    return Mockito.mock(JdbcPostRepository.class);
  }

  @Bean
  @Primary
  public JdbcPostTagRepository mockPostTagRepository() {
    return Mockito.mock(JdbcPostTagRepository.class);
  }

  @Bean
  @Primary
  public JdbcTagRepository mockTagRepository() {
    return Mockito.mock(JdbcTagRepository.class);
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
