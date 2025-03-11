package ru.yandex.practicum.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.service.PostService;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PostControllerTest {
  private static final String BASE_URL = "/posts";

  @MockBean
  PostService postService;

  @InjectMocks
  private PostController controller;
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getPosts() {
    Integer from = 0;
    Integer size = 10;

    when(postService.findAllPosts(anyInt(), anyInt()))
        .thenReturn(null);

    try {
      mockMvc.perform(post(BASE_URL, from, size))
             .andExpect(status().isOk());

    } catch (Exception e) {
      fail("Не ожидали получить исключение");
    }
  }

  @Test
  void getPostById() {
  }

  @Test
  void savePost() {
  }

  @Test
  void updatePost() {
  }

  @Test
  void addLike() {
  }

  @Test
  void saveComment() {
  }

  @Test
  void updateComment() {
  }

  @Test
  void deletePost() {
  }

  @Test
  void deleteComment() {
  }

  @Test
  void getTags() {
  }
}