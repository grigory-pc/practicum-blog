package ru.yandex.practicum.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.config.DataSourceTestConfig;
import ru.yandex.practicum.config.WebConfiguration;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(
    classes = {DataSourceTestConfig.class, WebConfiguration.class, PostRepository.class})
@Import(DataSourceTestConfig.class)
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.yml")
@Profile("test")
public class PostControllerIT {
  private static final String BASE_URL = "/api/posts";
  public static final long ID_POST = 1L;
  public static final long ID_NEW_POST = 2L;
  public static final int POSTS_SIZE = 1;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private PostRepository postRepository;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    Post post = Data.getPost();

    // Очистка и заполнение тестовых данных в базе
    jdbcTemplate.execute("DELETE FROM posts");
    String sql = "INSERT INTO posts (id, title, image, text)VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(sql,
                        post.getId(),
                        post.getTitle(),
                        post.getImage(),
                        post.getPostText());
  }

  @Test
  void getUsers_shouldReturnPagePosts() throws Exception {
    String from = "0";
    String size = "10";
    List<PostPreviewDto> postPreviewDtos = List.of(Data.getPostPreviewDto());
    Page<PostPreviewDto> page = new PageImpl<>(postPreviewDtos, PageRequest.of(0, 10), POSTS_SIZE);
    String expectedBody = objectMapper.writeValueAsString(page);

    mockMvc.perform(get(BASE_URL)
                        .param("from", from)
                        .param("size", size)
                        .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(expectedBody));
  }

  @Test
  void getUsers_shouldReturnPostFullDto() throws Exception {
    PostFullDto postFullDto = Data.getPostFullDto();
    String expectedBody = objectMapper.writeValueAsString(postFullDto);

    mockMvc.perform(get(BASE_URL + "/" + ID_POST)
                        .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(expectedBody));
  }

  @Test
  void getUsers_shouldSavePost() throws Exception {
    PostSaveDto postSaveDto = Data.getPostSaveDto();

    if (postRepository.findAll().size() == POSTS_SIZE) {

      mockMvc.perform(post(BASE_URL)
                          .accept(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk());

      Optional<Post> savedPost = postRepository.findById(ID_NEW_POST);

      assertTrue(savedPost.isPresent());
      assertEquals(postSaveDto.title(), savedPost.get().getTitle());
      assertEquals(postSaveDto.postText(), savedPost.get().getPostText());
    } else {
      fail("в базе данных больше одной записи, а ожидали только одну");
    }
  }

  @Test
  void delete_shouldRemovePostFromDatabase() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/" + ID_POST))
           .andExpect(status().isOk());

    Optional<Post> deletedPost = postRepository.findById(ID_POST);

    assertTrue(deletedPost.isEmpty());
  }
}