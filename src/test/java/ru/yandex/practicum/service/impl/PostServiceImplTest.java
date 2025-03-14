package ru.yandex.practicum.service.impl;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = PostServiceImpl.class)
class PostServiceImplTest {
  private static final Long POST_ID = 1L;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostTagRepository postTagRepository;
  private TagRepository tagRepository;
  @Autowired
  private PostMapper postMapper;
  @Autowired
  private LikeService likeService;
  private PostService postService;

  @BeforeEach
  void setUp() {
    postService = new PostServiceImpl(postRepository, postTagRepository, tagRepository, postMapper,
                                      likeService);
  }

  @Test
  void positiveTest_ShouldFindAllPosts() {
    try {
      int from = 0;
      int size = 10;

      Page<Post> postPage = new PageImpl<>(List.of(Data.getPost()));
      PostPreviewDto expectedPostPreviewDto = Data.getPostPreviewDto();

      when(postRepository.findAllPosts(any(Pageable.class)))
          .thenReturn(postPage);

      when(postMapper.toDtoPage(any()))
          .thenReturn(new PageImpl<>(List.of(expectedPostPreviewDto)));

      Page<PostPreviewDto> actualPostPreviewDto = postService.findAllPosts(from, size);

      assertEquals(1, actualPostPreviewDto.getTotalElements());
      assertEquals(expectedPostPreviewDto, actualPostPreviewDto.getContent().get(0));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldGetPostById() {
    try {
      Post post = Data.getPost();
      PostFullDto expectedPostFullDto = Data.getPostFullDto();

      when(postRepository.findById(anyLong()))
          .thenReturn(Optional.of(post));

      when(postMapper.toFullDto(any(Post.class)))
          .thenReturn(expectedPostFullDto);

      PostFullDto actualPostFullDto = postService.getPostById(POST_ID);

      assertEquals(expectedPostFullDto, actualPostFullDto);

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldSavePost() {
    try {
      Post post = Data.getPost();
      PostSaveDto postSaveDto = Data.getPostSaveDto();

      when(postMapper.toPost(any(PostSaveDto.class)))
          .thenReturn(post);
      doNothing().when(postRepository)
                 .save(any(Post.class));
      doNothing().when(postTagRepository)
                 .save(any(PostTag.class));
      doNothing().when(likeService)
                 .saveLike(anyLong());

      assertDoesNotThrow(
          () -> postService.savePost(postSaveDto));

      verify(postMapper, atLeastOnce()).toPost(any(PostSaveDto.class));
      verify(postRepository, atLeastOnce()).save(any(Post.class));
      verify(postTagRepository, atLeastOnce()).save(any(PostTag.class));
      verify(likeService, atLeastOnce()).saveLike(anyLong());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldUpdatePost() {
    try {
      Post post = Data.getPost();
      PostSaveDto postSaveDto = Data.getPostSaveDto();

      when(postRepository.findById(anyLong()))
          .thenReturn(Optional.of(post));

      doNothing().when(postRepository)
                 .save(any(Post.class));
      doNothing().when(postTagRepository)
                 .save(any(PostTag.class));

      assertDoesNotThrow(
          () -> postService.updatePost(POST_ID, postSaveDto));

      verify(postRepository, atLeastOnce()).findById(anyLong());
      verify(postRepository, atLeastOnce()).save(any(Post.class));
      verify(postTagRepository, atLeastOnce()).save(any(PostTag.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldDeletePostById() {
    doNothing().when(postRepository)
               .deleteById(anyLong());

    verify(postRepository, atLeastOnce()).deleteById(anyLong());
  }
}