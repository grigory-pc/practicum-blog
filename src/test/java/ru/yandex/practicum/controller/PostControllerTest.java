package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.config.DataTestSource;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import(DataTestSource.class)
@ContextConfiguration(classes = {PostController.class})
class PostControllerTest {
  private static final String BASE_URL = "/posts";
  private static final Long POST_ID = 1L;
  private static final Long COMMENT_ID = 1L;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean
  PostService postService;
  @MockitoBean
  CommentService commentService;
  @MockitoBean
  LikeService likeService;
  @MockitoBean
  TagService tagService;

  @Autowired
  private PostController controller;
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(controller)
        .build();
  }

  @Test
  void positiveTest_ShouldGetPosts() {
    try {
      String from = "0";
      String size = "10";
      List<PostPreviewDto> postPreviewDtos = List.of(Data.getPostPreviewDto());
      Page<PostPreviewDto> page = new PageImpl<>(postPreviewDtos, PageRequest.of(0, 10), 1);

      doReturn(page)
          .when(postService).findAllPosts(anyInt(), anyInt());

      mockMvc.perform(get(BASE_URL)
                          .param("from", from)
                          .param("size", size)
                          .accept(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk());

      verify(postService, atLeastOnce()).findAllPosts(anyInt(), anyInt());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldGetPostById() {
    try {
      PostFullDto postFullDto = Data.getPostFullDto();

      when(postService.getPostById(anyLong()))
          .thenReturn(postFullDto);

      mockMvc.perform(get(BASE_URL + POST_ID)
                          .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().is3xxRedirection());

      verify(postService, atLeastOnce()).getPostById(anyLong());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldSavePost() {
    try {
      doNothing().when(postService)
                 .savePost(any(PostSaveDto.class));

      mockMvc.perform(post(BASE_URL)
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(objectMapper.writeValueAsString(Data.getPostSaveDto())))
             .andExpect(status().is3xxRedirection());

      verify(postService, atLeastOnce()).savePost(any(PostSaveDto.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldUpdatePost() {
    try {
      doNothing().when(postService)
                 .updatePost(anyLong(), any(PostSaveDto.class));

      mockMvc.perform(patch(BASE_URL + POST_ID)
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(objectMapper.writeValueAsString(Data.getPostSaveDto())))
             .andExpect(status().is3xxRedirection());

      verify(postService, atLeastOnce()).updatePost(anyLong(), any(PostSaveDto.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldAddLike() {
    try {
      doNothing().when(likeService)
                 .addLike(anyLong());

      mockMvc.perform(post(BASE_URL + POST_ID + "/like")
                          .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().is3xxRedirection());

      verify(likeService, atLeastOnce()).addLike(anyLong());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldSaveComment() {
    try {
      doNothing().when(commentService)
                 .saveComment(anyLong(), any(CommentDto.class));

      mockMvc.perform(post(BASE_URL + POST_ID + "/comment")
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(objectMapper.writeValueAsString(Data.getCommentDto(POST_ID))))
             .andExpect(status().is3xxRedirection());

      verify(commentService, atLeastOnce()).saveComment(anyLong(), any(CommentDto.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldUpdateComment() {
    try {
      doNothing().when(commentService)
                 .updateComment(anyLong(), anyLong(), any(CommentDto.class));

      mockMvc.perform(patch(BASE_URL + POST_ID + "/comment/" + COMMENT_ID)
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(objectMapper.writeValueAsString(Data.getCommentDto(POST_ID))))
             .andExpect(status().is3xxRedirection());

      verify(commentService, atLeastOnce()).updateComment(anyLong(), anyLong(),
                                                          any(CommentDto.class));

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldDeletePost() {
    try {
      doNothing().when(postService)
                 .deletePostById(anyLong());

      mockMvc.perform(post(BASE_URL + POST_ID)
                          .param("_method", "delete"))
             .andExpect(status().is3xxRedirection());

      verify(postService, atLeastOnce()).deletePostById(anyLong());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldDeleteComment() {
    try {
      doNothing().when(commentService)
                 .deleteCommentById(anyLong());

      mockMvc.perform(post(BASE_URL + "comment/" + COMMENT_ID)
                          .param("_method", "delete"))
             .andExpect(status().is3xxRedirection());

      verify(commentService, atLeastOnce()).deleteCommentById(anyLong());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }
}