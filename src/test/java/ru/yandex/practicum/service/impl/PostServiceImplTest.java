package ru.yandex.practicum.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostServiceImpl.class)
class PostServiceImplTest {
  private static final Long POST_ID = 1L;
  @MockitoBean
  private PostRepository postRepository;
  @MockitoBean
  private PostTagRepository postTagRepository;
  @MockitoBean
  private TagRepository tagRepository;
  @MockitoBean
  private PostMapper postMapper;
  @Autowired
  private PostService postService;

  @Test
  void positiveTest_shouldFindAllPosts() {
    try {
      int from = 0;
      int size = 10;

      Page<Post> postPage = new PageImpl<>(List.of(Data.getPost()));
      PostPreviewDto expectedPostPreviewDto = Data.getPostPreviewDto();

      doReturn(postPage)
          .when(postRepository).findAll(any(Pageable.class));
      doReturn(Set.of(Data.getPostTag()))
          .when(postTagRepository).findAllByPostIdIn(anySet());
      doReturn(Set.of(Data.getTag()))
          .when(tagRepository).findAllByIdIn(anySet());
      doReturn(new PageImpl<>(List.of(expectedPostPreviewDto)))
          .when(postMapper).toDtoPage(any());


      Page<PostPreviewDto> actualPostPreviewDto = postService.findAllPosts(from, size);

      assertEquals(1, actualPostPreviewDto.getTotalElements());
      assertEquals(expectedPostPreviewDto, actualPostPreviewDto.getContent().get(0));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_shouldGetPostById() {
    try {
      Post post = Data.getPost();
      PostFullDto expectedPostFullDto = Data.getPostFullDto();

      doReturn(Optional.of(post))
          .when(postRepository).findById(anyLong());
      doReturn(Set.of(Data.getPostTag()))
          .when(postTagRepository).findAllByPostId(anyLong());
      doReturn(Optional.of(Data.getTag()))
          .when(tagRepository).findById(anyLong());
      doReturn(expectedPostFullDto)
          .when(postMapper).toFullDto(any(Post.class));

      PostFullDto actualPostFullDto = postService.getPostById(POST_ID);

      assertEquals(expectedPostFullDto, actualPostFullDto);

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_shouldSavePost() {
    try {
      Post post = Data.getPost();
      PostSaveDto postSaveDto = Data.getPostSaveDto();

      doReturn(post)
          .when(postMapper).toPost(any(PostSaveDto.class));
      doReturn(post)
          .when(postRepository).save(any(Post.class));
      doReturn(Data.getPostTag())
          .when(postTagRepository).save(any(PostTag.class));

      assertDoesNotThrow(
          () -> postService.savePost(postSaveDto));

      verify(postMapper, atLeastOnce()).toPost(any(PostSaveDto.class));
      verify(postRepository, atLeastOnce()).save(any(Post.class));
      verify(postTagRepository, atLeastOnce()).save(any(PostTag.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_shouldUpdatePost() {
    try {
      Post post = Data.getPost();
      PostSaveDto postSaveDto = Data.getPostSaveDto();

      doReturn(Optional.of(post))
          .when(postRepository).findById(anyLong());
      doReturn(post)
          .when(postRepository).save(any(Post.class));
      doReturn(Data.getPostTag())
          .when(postTagRepository).save(any(PostTag.class));
      doNothing().when(postTagRepository)
                 .deleteAllByPostId(anyLong());

      assertDoesNotThrow(
          () -> postService.updatePost(POST_ID, postSaveDto));

      verify(postRepository, atLeastOnce()).findById(anyLong());
      verify(postRepository, atLeastOnce()).save(any(Post.class));
      verify(postTagRepository, atLeastOnce()).save(any(PostTag.class));
      verify(postTagRepository, atLeastOnce()).deleteAllByPostId(anyLong());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_shouldDeletePostById() {
    doNothing().when(postRepository)
               .deleteById(anyLong());
    doNothing().when(postTagRepository)
               .deleteAllByPostId(anyLong());

    assertDoesNotThrow(
        () -> postService.deletePostById(POST_ID));

    verify(postRepository, atLeastOnce()).deleteById(anyLong());
    verify(postTagRepository, atLeastOnce()).deleteAllByPostId(anyLong());
  }
}