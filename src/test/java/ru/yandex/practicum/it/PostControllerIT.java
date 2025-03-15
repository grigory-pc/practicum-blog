package ru.yandex.practicum.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.utils.Data;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = {DataSourceTestConfig.class, WebConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.yml")
public class PostControllerIT {
  private static final String BASE_URL = "/posts";
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private JdbcTemplate jdbcTemplate;


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
                        post.getText());

  }

  @Test
  void getUsers_shouldReturnPagePosts() throws Exception {
    String from = "0";
    String size = "10";
    List<PostPreviewDto> postPreviewDtos = List.of(Data.getPostPreviewDto());
    Page<PostPreviewDto> page = new PageImpl<>(postPreviewDtos, PageRequest.of(0, 10), 1);
    String expectedBody = objectMapper.writeValueAsString(page);

    mockMvc.perform(get(BASE_URL)
                        .param("from", from)
                        .param("size", size)
                        .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(expectedBody));

    System.out.println("test");
  }
}
