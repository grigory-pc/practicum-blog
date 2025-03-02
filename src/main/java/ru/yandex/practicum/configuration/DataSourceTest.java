package ru.yandex.practicum.configuration;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * Конфигурация dataSource (H2 in memory DB).
 */
@Component
@RequiredArgsConstructor
@Profile("test")
public class DataSourceTest implements DataSourceConfig {
  private final Environment environment;

  @Bean
  @Override
  public DataSource getDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.test.driverClassName"));
    dataSource.setUrl(environment.getRequiredProperty("jdbc.test.url"));
    dataSource.setUsername(environment.getRequiredProperty("jdbc.test.username"));
    dataSource.setPassword(environment.getRequiredProperty("jdbc.test.password"));

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
    DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("schema.sql"));
    populator.execute(dataSource);
  }
}