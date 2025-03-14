package ru.yandex.practicum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * Конфигурация для работы с БД.
 */
@Configuration
@PropertySource(value = "classpath:application-test.yml")
@RequiredArgsConstructor
public class DataTestSource implements DataTestSourceConfig {
  private final Environment environment;

  @Bean
  @Override
  public javax.sql.DataSource getDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
    dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
    dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
    dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

    return dataSource;
  }

  /**
   * После инициализации контекста создание таблиц в БД.
   * "schema.sql" - Файл должен находиться в ресурсах
   *
   * @param event - событие после инициализации контекста.
   */
  @EventListener
  public void populate(ContextRefreshedEvent event) {
    javax.sql.DataSource dataSource = event.getApplicationContext()
                                           .getBean(javax.sql.DataSource.class);

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("schema.sql"));
    populator.execute(dataSource);
  }
}