package ru.yandex.practicum.controller.mvc.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.controller.mvc.test.configuration.DataSourceConfiguration;
import ru.yandex.practicum.controller.mvc.test.configuration.WebConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, WebConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    /**
     * Поднимает весь контекст приложения, включая веб-контекст.
     * Очистка и заполнение тестовых данных в базе.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("INSERT INTO users (id, first_name, last_name, age, active) VALUES (1, 'Иван', 'Иванов', 30, true)");
        jdbcTemplate.execute("INSERT INTO users (id, first_name, last_name, age, active) VALUES (2, 'Мария', 'Сидорова', 25, false)");
    }

    @Test
    void getUsers_shouldReturnHtmlWithUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(xpath("//table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//table/tbody/tr[1]/td[2]").string("Иван"));
    }

    @Test
    void save_shouldAddUserToDatabaseAndRedirect() throws Exception {
        mockMvc.perform(post("/users")
                        .param("id", "4")
                        .param("firstName", "Анна")
                        .param("lastName", "Смирнова")
                        .param("age", "28")
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void delete_shouldRemoveUserFromDatabaseAndRedirect() throws Exception {
        mockMvc.perform(post("/users/1")
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void getUsers_shouldFindAllUserFromDatabaseAndRedirect() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(xpath("//table/tbody/tr[1]/td[2]").string("Иван"))
                .andExpect(xpath("//table/tbody/tr[2]/td[2]").string("Мария"));
    }
}