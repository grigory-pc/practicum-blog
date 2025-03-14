package ru.yandex.practicum.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.repository.Impl.JdbcCommentRepositoryImpl;
import ru.yandex.practicum.repository.Impl.JdbcLikeRepositoryImpl;
import ru.yandex.practicum.repository.Impl.JdbcPostRepositoryImpl;
import ru.yandex.practicum.repository.Impl.JdbcPostTagRepositoryImpl;
import ru.yandex.practicum.repository.Impl.JdbcTagRepositoryImpl;
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
  public JdbcCommentRepositoryImpl mockCommentRepository () {
    return Mockito.mock(JdbcCommentRepositoryImpl.class);
  }
  @Bean
  @Primary
  public JdbcLikeRepositoryImpl mockLikeRepository () {
    return Mockito.mock(JdbcLikeRepositoryImpl.class);
  }

  @Bean
  @Primary
  public JdbcPostRepositoryImpl mockPostRepository () {
    return Mockito.mock(JdbcPostRepositoryImpl.class);
  }

  @Bean
  @Primary
  public JdbcPostTagRepositoryImpl mockPostTagRepository() {
    return Mockito.mock(JdbcPostTagRepositoryImpl.class);
  }

  @Bean
  @Primary
  public JdbcTagRepositoryImpl mockTagRepository() {
    return Mockito.mock(JdbcTagRepositoryImpl.class);
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
