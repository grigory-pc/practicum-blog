package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.LikeService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class PostControllerTest {
  private static final String BASE_URL = "/posts/";
  public static final String REDIRECT_POST = "redirect:/post";
  private static final Long POST_ID = 1L;
  private static final Long COMMENT_ID = 1L;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private static Data data;

  @Mock
  PostService postService;
  @Mock
  CommentService commentService;
  @Mock
  LikeService likeService;

  @InjectMocks
  private PostController controller;
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void positiveTest_ShouldGetPosts() {
    try {
      String from = "0";
      String size = "10";
      List<PostPreviewDto> postPreviewDtos = List.of(data.getPostPreviewDto());
      Page<PostPreviewDto> page = new PageImpl<>(postPreviewDtos, PageRequest.of(0, 10), 1);

      when(postService.findAllPosts(anyInt(), anyInt()))
          .thenReturn(page);


      mockMvc.perform(get(BASE_URL)
                          .param("from", from)
                          .param("size", size)
                          .accept(MediaType.TEXT_HTML))
             .andExpect(status().isOk());

      verify(postService, atLeastOnce()).findAllPosts(anyInt(), anyInt());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void positiveTest_ShouldGetPostById() {
    try {
      PostFullDto postFullDto = data.getPostFullDto();

      when(postService.getPostById(anyLong()))
          .thenReturn(postFullDto);

      mockMvc.perform(get(BASE_URL + POST_ID)
                          .contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().is3xxRedirection())
             .andExpect(redirectedUrl(REDIRECT_POST));

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
                          .content(objectMapper.writeValueAsString(data.getPostSaveDto())))
             .andExpect(status().is3xxRedirection())
             .andExpect(redirectedUrl(REDIRECT_POST));

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
                          .content(objectMapper.writeValueAsString(data.getPostSaveDto())))
             .andExpect(status().is3xxRedirection())
             .andExpect(redirectedUrl(REDIRECT_POST));

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
             .andExpect(status().is3xxRedirection())
             .andExpect(redirectedUrl(REDIRECT_POST));

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
                          .content(objectMapper.writeValueAsString(data.getCommentDto(POST_ID))))
             .andExpect(status().is3xxRedirection())
             .andExpect(redirectedUrl(REDIRECT_POST));

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
                          .content(objectMapper.writeValueAsString(data.getCommentDto(POST_ID))))
             .andExpect(status().is3xxRedirection())
             .andExpect(redirectedUrl(REDIRECT_POST));

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
             .andExpect(status().is3xxRedirection())
             .andExpect(redirectedUrl(REDIRECT_POST));

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
             .andExpect(status().is3xxRedirection())
             .andExpect(redirectedUrl(REDIRECT_POST));

      verify(commentService, atLeastOnce()).deleteCommentById(anyLong());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }
}