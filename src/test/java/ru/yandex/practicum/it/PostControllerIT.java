package ru.yandex.practicum.it;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.utils.Data;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIT {
  private static final String BASE_URL = "/posts";
  public static final long ID_POST = 1L;
  public static final long ID_COMMENT = 1L;

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
    List<Tag> tags = List.of(Data.getTag());

    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    jdbcTemplate = webApplicationContext.getBean(JdbcTemplate.class);

    jdbcTemplate.execute("DELETE FROM posts");
    jdbcTemplate.execute("DELETE FROM tags");
    jdbcTemplate.execute("DELETE FROM comments");

    String sqlPosts
        = "INSERT INTO posts (id, title, image_path, text, count_likes)VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sqlPosts,
                        post.getId(),
                        post.getTitle(),
                        post.getImagePath(),
                        post.getText(),
                        post.getLikesCount());

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
    String pageNumber = "0";
    String pageSize = "10";
    String search = "";

    mockMvc.perform(get(BASE_URL)
                        .param("search", search)
                        .param("pageSize", pageSize)
                        .param("pageNumber", pageNumber))
           .andExpect(status().isOk())
           .andExpect(content().contentType("text/html;charset=UTF-8"))
           .andExpect(view().name("posts"))
           .andExpect(model().attributeExists("posts"))
           .andExpect(model().attribute("posts", hasSize(1)))
           .andExpect(model().attributeExists("paging"))
           .andExpect(model().attributeExists("search"));
  }

  @Test
  @DisplayName("Позитивный тест - проверяем получение данных поста из базы данных")
  void positiveTest_shouldGetPostFullDto() throws Exception {

    mockMvc.perform(get(BASE_URL + "/" + ID_POST))
           .andExpect(status().isOk())
           .andExpect(content().contentType("text/html;charset=UTF-8"))
           .andExpect(view().name("post"))
           .andExpect(model().attributeExists("post"))
           .andExpect(model().attribute("post", hasProperty("title", is("test"))))
           .andExpect(model().attribute("post", hasProperty("text", containsString("text"))));
  }

  @Test
  @DisplayName("Позитивный тест - проверяем сохранение поста в базу данных")
  void positiveTest_shouldSavePost() throws Exception {
    jdbcTemplate.execute("DELETE FROM posts");

    PostDto postSaveDto = Data.getPostSaveDto();

    mockMvc.perform(multipart(BASE_URL)
                        .file(Data.getImage())
                        .file(Data.getTextFile(postSaveDto.getText()))
                        .file(Data.getTitleFile(postSaveDto.getTitle()))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().is3xxRedirection());

    List<Post> allPosts = postRepository.findAll();
    Optional<Post> savedPost = postRepository.findById(allPosts.get(allPosts.size() - 1).getId());

    assertTrue(savedPost.isPresent());
    assertEquals(postSaveDto.getTitle(), savedPost.get().getTitle());
    assertEquals(postSaveDto.getText(), savedPost.get().getText());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем обновление поста в базе данных")
  void positiveTest_shouldUpdatePost() throws Exception {
    PostDto expectedPostSaveDto = Data.getPostSaveDto();
    expectedPostSaveDto.setTitle("new title");
    expectedPostSaveDto.setText("new text");

    mockMvc.perform(multipart(BASE_URL + "/" + ID_POST)
                        .file(Data.getImage())
                        .file(Data.getTitleFile("new title"))
                        .file(Data.getTextFile("new text"))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/posts/" + ID_POST));

    Optional<Post> savedPost = postRepository.findById(ID_POST);

    assertTrue(savedPost.isPresent());
    assertEquals(expectedPostSaveDto.getTitle(), savedPost.get().getTitle());
    assertEquals(expectedPostSaveDto.getText(), savedPost.get().getText());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем добавление лайка к посту в базе данных")
  void positiveTest_shouldAddLikeToPost() throws Exception {
    Integer incrementLike = 1;
    Optional<Post> postBeforeLike = postRepository.findById(ID_POST);
    Integer expectedLikes = postBeforeLike.get().getLikesCount() + incrementLike;

    mockMvc.perform(post(BASE_URL + "/" + ID_POST + "/true"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/posts/" + ID_POST));

    Optional<Post> postAfterLike = postRepository.findById(ID_POST);
    Integer actualLikes = postAfterLike.get().getLikesCount();

    assertEquals(expectedLikes, actualLikes);
  }

  @Test
  @DisplayName("Позитивный тест - проверяем сохранение комментария к посту в базе данных")
  void positiveTest_shouldSaveCommentForPost() throws Exception {
    CommentDto commentDto = Data.getCommentDto(ID_POST);

    mockMvc.perform(post(BASE_URL + "/" + ID_POST + "/comments")
                        .param("text", commentDto.getText()))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/posts/" + ID_POST));

    Optional<Comment> actualComment = commentRepository.findById(ID_COMMENT);

    assertTrue(actualComment.isPresent());
    assertEquals(ID_POST, actualComment.get().getPost().getId());
    assertEquals(commentDto.getText(), actualComment.get().getText());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем обновление комментария к посту в базе данных")
  void positiveTest_shouldUpdateCommentForPost() throws Exception {
    String newCommentText = "new text";

    insertComment();

    mockMvc.perform(post(BASE_URL + "/" + ID_POST + "/comments/" + ID_COMMENT)
                        .param("text", newCommentText))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/posts/" + ID_POST));

    Optional<Comment> actualComment = commentRepository.findById(ID_POST);

    assertTrue(actualComment.isPresent());
    assertEquals(ID_POST, actualComment.get().getPost().getId());
    assertEquals(newCommentText, actualComment.get().getText());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем удаление поста из базы данных")
  void positiveTest_shouldDeletePost() throws Exception {
    mockMvc.perform(post(BASE_URL + "/" + ID_POST + "/delete"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/posts"));

    Optional<Post> deletedPost = postRepository.findById(ID_POST);

    assertTrue(deletedPost.isEmpty());
  }

  @Test
  @DisplayName("Позитивный тест - проверяем удаление комментария из базы данных")
  void positiveTest_shouldDeleteComment() throws Exception {
    insertComment();

    mockMvc.perform(post(BASE_URL + "/" + ID_POST + "/comments/" + ID_COMMENT + "/delete"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/posts/" + ID_POST));

    Optional<Comment> deletedComment = commentRepository.findById(ID_COMMENT);

    assertTrue(deletedComment.isEmpty());
  }

  private void insertComment() {
    Comment comment = Data.getComment(ID_COMMENT);

    String sqlComments
        = "INSERT INTO comments (id, post_id, text)VALUES (?, ?, ?)";
    jdbcTemplate.update(sqlComments,
                        comment.getId(),
                        comment.getPost().getId(),
                        comment.getText());
  }
}