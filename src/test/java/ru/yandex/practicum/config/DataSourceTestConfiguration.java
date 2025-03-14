package ru.yandex.practicum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import org.springframework.stereotype.Component;

/**
 * Конфигурация dataSource.
 */
@Component
@RequiredArgsConstructor
@Profile("test")
public class DataSourceTestConfiguration {
  private final Environment environment;

  @Bean
  public javax.sql.DataSource getDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
    dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
    dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
    dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

    return dataSource;
  }

  /**
   * JdbcTemplate — компонент для выполнения запросов
   * @param dataSource - источник БД.
   * @return бин для JdbcTemplate.
   */
  @Bean
  public JdbcTemplate jdbcTemplate(javax.sql.DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  /**
   * После инициализации контекста создание таблиц в БД.
   * "schema.sql" - Файл должен находиться в ресурсах
   *
   * @param event - событие после инициализации контекста.
   */
  @EventListener
  public void populate(ContextRefreshedEvent event) {
    javax.sql.DataSource dataSource = event.getApplicationContext().getBean(javax.sql.DataSource.class);

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("schema.sql"));
    populator.execute(dataSource);
  }
}