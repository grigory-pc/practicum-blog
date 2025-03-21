package ru.yandex.practicum.it;

import java.sql.Connection;
import java.sql.SQLException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.utils.Data;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerIT {
  private static final String BASE_URL = "/images";
  public static final long ID_POST = 1L;

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
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    jdbcTemplate = webApplicationContext.getBean(JdbcTemplate.class);
  }

  @Test
  @DisplayName(
      "Позитивный тест - проверяем получение байтового массива загруженного изображения поста")
  void positiveTest_shouldGetPostFullDto() throws Exception {
    MockMultipartFile imageFile = Data.getImage();
    PostDto postSaveDto = Data.getPostSaveDto();

    jdbcTemplate.execute("DELETE FROM posts");

    mockMvc.perform(multipart("/posts")
                        .file(imageFile)
                        .file(Data.getTextFile(postSaveDto.getText()))
                        .file(Data.getTitleFile(postSaveDto.getTitle()))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/posts/" + ID_POST));

    Optional<Post> savedPost = postRepository.findById(ID_POST);
    assertTrue(savedPost.isPresent());

    mockMvc.perform(get(BASE_URL + "/" + savedPost.get().getId())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
           .andExpect(status().isOk())
           .andExpect(content().contentType("image/jpeg"))
           .andExpect(content().bytes(imageFile.getBytes()));
  }
}