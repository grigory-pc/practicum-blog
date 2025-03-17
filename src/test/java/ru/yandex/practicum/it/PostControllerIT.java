package ru.yandex.practicum.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.config.DataSourceTestConfig;
import ru.yandex.practicum.config.WebConfiguration;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(
    classes = {DataSourceTestConfig.class, WebConfiguration.class, PostRepository.class})
@Import(DataSourceTestConfig.class)
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
@Profile("test")
public class PostControllerIT {
  private static final String BASE_URL = "/posts";
  public static final long ID_POST = 1L;
  public static final int POSTS_SIZE = 1;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private PostRepository postRepository;

  private MockMvc mockMvc;

  @BeforeAll
  public static void initTables(@Autowired DataSource dataSource) {
    try (Connection conn = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("/schema.sql"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @BeforeEach
  void setUp() {
    Post post = Data.getPost();
    Set<Tag> tags = Data.getTags();

    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    jdbcTemplate = webApplicationContext.getBean(JdbcTemplate.class);

    jdbcTemplate.execute("DELETE FROM posts");
    jdbcTemplate.execute("DELETE FROM tags");

    String sqlPosts
        = "INSERT INTO posts (id, title, image, text, count_likes)VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sqlPosts,
                        post.getId(),
                        post.getTitle(),
                        post.getImage(),
                        post.getPostText(),
                        post.getCountLikes());

    for (Tag tag : tags) {
      String sqlTags = "INSERT INTO tags (id, tag_name)VALUES (?, ?)";
      jdbcTemplate.update(sqlTags,
                          tag.getId(),
                          tag.getTagName());
    }
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
    jdbcTemplate.execute("DELETE FROM posts");

    PostSaveDto postSaveDto = Data.getPostSaveDto();

    mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postSaveDto)))
           .andExpect(status().isOk());

    Optional<Post> savedPost = postRepository.findById(ID_POST);

    assertTrue(savedPost.isPresent());
    assertEquals(postSaveDto.title(), savedPost.get().getTitle());
    assertEquals(postSaveDto.postText(), savedPost.get().getPostText());

  }

  @Test
  void delete_shouldRemovePostFromDatabase() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/" + ID_POST))
           .andExpect(status().isOk());

    Optional<Post> deletedPost = postRepository.findById(ID_POST);

    assertTrue(deletedPost.isEmpty());
  }
}