//package ru.yandex.practicum.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ru.yandex.practicum.config.DataSourceTestConfig;
//import ru.yandex.practicum.dto.CommentDto;
//import ru.yandex.practicum.dto.PostDto;
//import ru.yandex.practicum.dto.TagDto;
//import ru.yandex.practicum.service.CommentService;
//import ru.yandex.practicum.service.PostService;
//import ru.yandex.practicum.utils.Data;
//
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.atLeastOnce;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//
//@ExtendWith(SpringExtension.class)
//@Import(DataSourceTestConfig.class)
//@ContextConfiguration(classes = {PostController.class})
//class PostControllerTest {
//  private static final String BASE_URL = "/posts";
//  private static final Long POST_ID = 1L;
//  private static final Long COMMENT_ID = 1L;
//  private final ObjectMapper objectMapper = new ObjectMapper();
//
//  @MockitoBean
//  PostService postService;
//  @MockitoBean
//  CommentService commentService;
//
//  @Autowired
//  private PostController controller;
//  private MockMvc mockMvc;
//
//  @BeforeEach
//  void setUp() {
//    mockMvc = MockMvcBuilders
//        .standaloneSetup(controller)
//        .build();
//  }
//
//  @Test
//  void positiveTest_shouldGetPosts() {
//    try {
//      String pageNumber = "0";
//      String pageSize = "10";
//      String search = "";
//      List<PostDto> postPreviewDtos = List.of(Data.getPostDto());
//      Page<PostDto> page = new PageImpl<>(postPreviewDtos, PageRequest.of(0, 10), 1);
//
//      doReturn(page)
//          .when(postService).findAllPosts(anyString(), any(Pageable.class));
//
//      mockMvc.perform(get(BASE_URL)
//                          .param("search", search)
//                          .param("pageSize", pageSize)
//                          .param("pageNumber", pageNumber))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType("text/html;charset=UTF-8"))
//             .andExpect(view().name("postList"))
//             .andExpect(model().attributeExists("posts"));
//
//      verify(postService, atLeastOnce()).findAllPosts(anyString(), any(Pageable.class));
//
//    } catch (Exception e) {
//      fail("Не ожидали получить исключение");
//    }
//  }
//
//  //  @Test
//  //  void positiveTest_shouldGetPostById() {
//  //    try {
//  //      PostFullDto postFullDto = Data.getPostFullDto();
//  //      String expectedBody = objectMapper.writeValueAsString(postFullDto);
//  //
//  //      when(postService.getPostById(anyLong()))
//  //          .thenReturn(postFullDto);
//  //
//  //      mockMvc.perform(get(BASE_URL + "/" + POST_ID)
//  //                          .contentType(MediaType.APPLICATION_JSON))
//  //             .andExpect(status().isOk())
//  //             .andExpect(content().json(expectedBody));
//  //
//  //      verify(postService, atLeastOnce()).getPostById(anyLong());
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//  //
//  //  @Test
//  //  void positiveTest_shouldSavePost() {
//  //    try {
//  //      doNothing().when(postService)
//  //                 .savePost(any(PostSaveDto.class));
//  //
//  //      mockMvc.perform(post(BASE_URL)
//  //                          .contentType(MediaType.APPLICATION_JSON)
//  //                          .content(objectMapper.writeValueAsString(Data.getPostSaveDto())))
//  //             .andExpect(status().isOk());
//  //
//  //      verify(postService, atLeastOnce()).savePost(any(PostSaveDto.class));
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//  //
//  //  @Test
//  //  void positiveTest_shouldUpdatePost() {
//  //    try {
//  //      doNothing().when(postService)
//  //                 .updatePost(anyLong(), any(PostSaveDto.class));
//  //
//  //      mockMvc.perform(patch(BASE_URL + "/" + POST_ID)
//  //                          .contentType(MediaType.APPLICATION_JSON)
//  //                          .content(objectMapper.writeValueAsString(Data.getPostSaveDto())))
//  //             .andExpect(status().isOk());
//  //
//  //      verify(postService, atLeastOnce()).updatePost(anyLong(), any(PostSaveDto.class));
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//  //
//  //  @Test
//  //  void positiveTest_shouldAddLike() {
//  //    try {
//  //      doNothing().when(postService)
//  //                 .addLike(anyLong());
//  //
//  //      mockMvc.perform(post(BASE_URL + "/" + POST_ID + "/like")
//  //                          .contentType(MediaType.APPLICATION_JSON))
//  //             .andExpect(status().isOk());
//  //
//  //      verify(postService, atLeastOnce()).addLike(anyLong());
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//  //
//  //  @Test
//  //  void positiveTest_shouldSaveComment() {
//  //    try {
//  //      doNothing().when(commentService)
//  //                 .saveComment(anyLong(), any(CommentDto.class));
//  //
//  //      mockMvc.perform(post(BASE_URL + "/" + POST_ID + "/comment")
//  //                          .contentType(MediaType.APPLICATION_JSON)
//  //                          .content(objectMapper.writeValueAsString(Data.getCommentDto(POST_ID))))
//  //             .andExpect(status().isOk());
//  //
//  //      verify(commentService, atLeastOnce()).saveComment(anyLong(), any(CommentDto.class));
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//  //
//  //  @Test
//  //  void positiveTest_shouldUpdateComment() {
//  //    try {
//  //      doNothing().when(commentService)
//  //                 .updateComment(anyLong(), anyLong(), any(CommentDto.class));
//  //
//  //      mockMvc.perform(patch(BASE_URL + "/" + POST_ID + "/comment/" + COMMENT_ID)
//  //                          .contentType(MediaType.APPLICATION_JSON)
//  //                          .content(objectMapper.writeValueAsString(Data.getCommentDto(POST_ID))))
//  //             .andExpect(status().isOk());
//  //
//  //      verify(commentService, atLeastOnce()).updateComment(anyLong(), anyLong(),
//  //                                                          any(CommentDto.class));
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//  //
//  //  @Test
//  //  void positiveTest_shouldDeletePost() {
//  //    try {
//  //      doNothing().when(postService)
//  //                 .deletePostById(anyLong());
//  //
//  //      mockMvc.perform(delete(BASE_URL + "/" + POST_ID))
//  //             .andExpect(status().isOk());
//  //
//  //      verify(postService, atLeastOnce()).deletePostById(anyLong());
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//  //
//  //  @Test
//  //  void positiveTest_shouldDeleteComment() {
//  //    try {
//  //      doNothing().when(commentService)
//  //                 .deleteCommentById(anyLong());
//  //
//  //      mockMvc.perform(delete(BASE_URL + "/comment/" + COMMENT_ID))
//  //             .andExpect(status().isOk());
//  //
//  //      verify(commentService, atLeastOnce()).deleteCommentById(anyLong());
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//  //
//  //  @Test
//  //  void positiveTest_shouldGetTags() {
//  //    try {
//  //      List<TagDto> tags = List.of(Data.getTagDto());
//  //      String expectedBody = objectMapper.writeValueAsString(tags);
//  //
//  //      when(tagService.findAllTags())
//  //          .thenReturn(tags);
//  //
//  //      mockMvc.perform(get(BASE_URL + "/tags")
//  //                          .contentType(MediaType.APPLICATION_JSON))
//  //             .andExpect(status().isOk())
//  //             .andExpect(content().json(expectedBody));
//  //
//  //      verify(tagService, atLeastOnce()).findAllTags();
//  //
//  //    } catch (Exception e) {
//  //      fail("Не ожидали получить исключение");
//  //    }
//  //  }
//}