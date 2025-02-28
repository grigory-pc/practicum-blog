package ru.yandex.practicum.configuration;

import org.h2.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Конфигурация для работы с БД.
 */
@Configuration
public class DataSourceConfiguration {

  /**
   * Настройка DataSource — компонент, отвечающий за соединение с базой данных.
   * Настройки соединения из Environment.
   *
   * @param url - адрес БД.
   * @param username - имя пользователя для подключения к БД.
   * @param password - пароль пользователя для подключения к БД.
   * @return DataSource.
   */
  @Bean
  public DataSource dataSource(
      @Value("${spring.datasource.url}") String url,
      @Value("${spring.datasource.username}") String username,
      @Value("${spring.datasource.password}") String password
  ) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(Driver.class.getName());
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    return dataSource;
  }

  /**
   * JdbcTemplate — компонент для выполнения запросов.
   *
   * @param dataSource - компонент, отвечающий за соединение с базой данных.
   * @return JdbcTemplate.
   */
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  /**
   * После инициализации контекста выполняем наполнение схемы базы данных
   * "schema.sql" - Файл должен находиться в ресурсах
   *
   * @param event - событие после инициализации контекста.
   */
  @EventListener
  public void populate(ContextRefreshedEvent event) {
    DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("schema.sql"));
    populator.execute(dataSource);
  }
}