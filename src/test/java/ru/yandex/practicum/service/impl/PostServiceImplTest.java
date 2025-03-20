package ru.yandex.practicum.service.impl;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
  @DisplayName("Позитивный тест - проверяем получение списка превью постов")
  void positiveTest_shouldFindAllPosts() {
    try {
      String search = "";
      Pageable pageable = PageRequest.of(0, 10);

      Page<Post> postPage = new PageImpl<>(List.of(Data.getPost()));
      PostDto expectedPostDto = Data.getPostDto();

      doReturn(postPage)
          .when(postRepository).findAll(any(Pageable.class));
      doReturn(new PageImpl<>(List.of(expectedPostDto)))
          .when(postMapper).toDtoPage(any());
      doReturn(List.of(Data.getPostTag()))
          .when(postTagRepository).findAllByPostId(anyLong());
      doReturn(Optional.of(Data.getTag()))
          .when(tagRepository).findById(anyLong());

      Page<PostDto> actualPostPreviewDto = postService.findAllPosts(search, pageable);

      assertEquals(1, actualPostPreviewDto.getTotalElements());
      assertEquals(expectedPostDto, actualPostPreviewDto.getContent().get(0));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  @DisplayName("Позитивный тест - проверяем получение данных поста")
  void positiveTest_shouldGetPostById() {
    try {
      Post post = Data.getPost();
      PostDto expectedPostDto = Data.getPostDto();

      doReturn(Optional.of(post))
          .when(postRepository).findById(anyLong());
      doReturn(expectedPostDto)
          .when(postMapper).toDto(any(Post.class));
      doReturn(List.of(Data.getPostTag()))
          .when(postTagRepository).findAllByPostId(anyLong());
      doReturn(Optional.of(Data.getTag()))
          .when(tagRepository).findById(anyLong());

      PostDto actualPostFullDto = postService.getPostById(POST_ID);

      assertEquals(expectedPostDto, actualPostFullDto);

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  @DisplayName("Позитивный тест - проверяем сохранение поста")
  void positiveTest_shouldSavePost() {
    try {
      Post post = Data.getPost();
      PostDto postSaveDto = Data.getPostSaveDto();
      String tags = "test";

      doReturn(post)
          .when(postMapper).toPost(any(PostDto.class));
      doReturn(post)
          .when(postRepository).save(any(Post.class));
      doNothing().when(postTagRepository)
                 .deleteAllByPostId(anyLong());
      doReturn(Optional.of(Data.getTag()))
          .when(tagRepository).findDistinctByTagName(anyString());
      doReturn(List.of(Data.getPostTag()))
          .when(postTagRepository).saveAll(anyList());
      doReturn(postSaveDto)
          .when(postMapper).toDto(any(Post.class));

      assertDoesNotThrow(
          () -> postService.savePost(postSaveDto, tags, null));

      verify(postMapper, atLeastOnce()).toPost(any(PostDto.class));
      verify(postMapper, atLeastOnce()).toDto(any(Post.class));
      verify(postRepository, atLeastOnce()).save(any(Post.class));
      verify(postTagRepository, atLeastOnce()).deleteAllByPostId(anyLong());
      verify(tagRepository, atLeastOnce()).findDistinctByTagName(anyString());
      verify(postTagRepository, atLeastOnce()).saveAll(anyList());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  @DisplayName("Позитивный тест - проверяем удаление поста")
  void positiveTest_shouldDeletePostById() {
    doNothing().when(postRepository)
               .deletePostById(anyLong());
    doNothing().when(postTagRepository)
               .deleteAllByPostId(anyLong());

    assertDoesNotThrow(
        () -> postService.deletePostById(POST_ID));

    verify(postRepository, atLeastOnce()).deletePostById(anyLong());
    verify(postTagRepository, atLeastOnce()).deleteAllByPostId(anyLong());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем добавление лайка к посту")
  void positiveTest_shouldAddLike() {
    doNothing().when(postRepository)
               .increaseLikesCount(anyLong());

    assertDoesNotThrow(
        () -> postService.addLike(POST_ID, true));

    verify(postRepository, atLeastOnce()).increaseLikesCount(anyLong());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем получение байтового массива поста")
  void positiveTest_shouldGetImage() {
    doReturn(Optional.of(Data.getPost()))
        .when(postRepository).findById(anyLong());

    byte[] actualImage = postService.getPostImage(POST_ID);

    verify(postRepository, atLeastOnce()).findById(anyLong());
    assertNotNull(actualImage);
  }
}