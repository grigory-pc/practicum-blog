package ru.yandex.practicum.config;

import org.h2.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
      @Value("${jdbc.test.datasource.url}") String url,
      @Value("${jdbc.test.datasource.username}") String username,
      @Value("${jdbc.test.datasource.password}") String password
  ) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(Driver.class.getName());
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("schema.sql"));
    populator.execute(dataSource);

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
}