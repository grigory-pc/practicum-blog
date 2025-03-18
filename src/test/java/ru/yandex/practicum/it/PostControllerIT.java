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
import org.junit.jupiter.api.DisplayName;
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
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(
    classes = {DataSourceTestConfig.class, WebConfiguration.class, PostRepository.class,
               CommentRepository.class})
@Import(DataSourceTestConfig.class)
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
@Profile("test")
public class PostControllerIT {
  private static final String BASE_URL = "/posts";
  public static final long ID_POST = 1L;
  public static final long ID_COMMENT = 1L;
  public static final int POSTS_SIZE = 1;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private CommentRepository commentRepository;

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
    jdbcTemplate.execute("DELETE FROM comments");

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
  @DisplayName(
      "Позитивный тест - проверяем получение списка превью постов с пагинацией из базы данных")
  void positiveTest_shouldGetPagePosts() throws Exception {
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
  @DisplayName("Позитивный тест - проверяем получение данных поста из базы данных")
  void positiveTest_shouldGetPostFullDto() throws Exception {
    PostFullDto postFullDto = Data.getPostFullDto();
    String expectedBody = objectMapper.writeValueAsString(postFullDto);

    mockMvc.perform(get(BASE_URL + "/" + ID_POST)
                        .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(expectedBody));
  }

  @Test
  @DisplayName("Позитивный тест - проверяем сохранение поста в базу данных")
  void positiveTest_shouldSavePost() throws Exception {
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
  @DisplayName("Позитивный тест - проверяем обновление поста в базе данных")
  void positiveTest_shouldUpdatePost() throws Exception {
    PostSaveDto postSaveDto = Data.getPostSaveDto();
    PostSaveDto newPostSaveDto = new PostSaveDto("new title", postSaveDto.image(), "new text",
                                                 null);

    mockMvc.perform(patch(BASE_URL + "/" + ID_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPostSaveDto)))
           .andExpect(status().isOk());

    Optional<Post> savedPost = postRepository.findById(ID_POST);

    assertTrue(savedPost.isPresent());
    assertEquals(newPostSaveDto.title(), savedPost.get().getTitle());
    assertEquals(newPostSaveDto.postText(), savedPost.get().getPostText());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем добавление лайка к посту в базе данных")
  void positiveTest_shouldAddLikeToPost() throws Exception {
    Integer incrementLike = 1;
    Optional<Post> postBeforeLike = postRepository.findById(ID_POST);
    Integer expectedLikes = postBeforeLike.get().getCountLikes() + incrementLike;

    mockMvc.perform(post(BASE_URL + "/" + ID_POST + "/like"))
           .andExpect(status().isOk());

    Optional<Post> postAfterLike = postRepository.findById(ID_POST);
    Integer actualLikes = postAfterLike.get().getCountLikes();

    assertEquals(expectedLikes, actualLikes);
  }

  @Test
  @DisplayName("Позитивный тест - проверяем сохранение комментария к посту в базе данных")
  void positiveTest_shouldSaveCommentForPost() throws Exception {
    CommentDto commentDto = Data.getCommentDto(ID_POST);

    mockMvc.perform(post(BASE_URL + "/" + ID_POST + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
           .andExpect(status().isOk());

    Optional<Comment> actualComment = commentRepository.findById(ID_COMMENT);

    assertTrue(actualComment.isPresent());
    assertEquals(ID_POST, actualComment.get().getPost().getId());
    assertEquals(commentDto.commentText(), actualComment.get().getCommentText());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем обновление комментария к посту в базе данных")
  void positiveTest_shouldUpdateCommentForPost() throws Exception {
    insertComment();

    CommentDto newCommentDto = new CommentDto(ID_COMMENT, "new text");

    mockMvc.perform(patch(BASE_URL + "/" + ID_POST + "/comment/" + ID_COMMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentDto)))
           .andExpect(status().isOk());

    Optional<Comment> actualComment = commentRepository.findById(ID_POST);

    assertTrue(actualComment.isPresent());
    assertEquals(ID_POST, actualComment.get().getPost().getId());
    assertEquals(newCommentDto.commentText(), actualComment.get().getCommentText());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем удаление поста из базы данных")
  void positiveTest_shouldDeletePost() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/" + ID_POST))
           .andExpect(status().isOk());

    Optional<Post> deletedPost = postRepository.findById(ID_POST);

    assertTrue(deletedPost.isEmpty());
  }


  @Test
  @DisplayName("Позитивный тест - проверяем удаление комментария из базы данных")
  void positiveTest_shouldDeleteComment() throws Exception {
    insertComment();

    mockMvc.perform(delete(BASE_URL + "/comment/" + ID_COMMENT))
           .andExpect(status().isOk());

    Optional<Comment> deletedComment = commentRepository.findById(ID_COMMENT);

    assertTrue(deletedComment.isEmpty());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем получение списка тегов из базы данных")
  void positiveTest_shouldGetTags() throws Exception {
    Set<TagDto> tags = Data.getTagDtos();
    String expectedBody = objectMapper.writeValueAsString(tags);

    mockMvc.perform(get(BASE_URL + "/tags")
                        .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(expectedBody));
  }

  private void insertComment() {
    Comment comment = Data.getComment(ID_COMMENT);

    String sqlComments
        = "INSERT INTO comments (id, post_id, text)VALUES (?, ?, ?)";
    jdbcTemplate.update(sqlComments,
                        comment.getId(),
                        comment.getPost().getId(),
                        comment.getCommentText());
  }
}