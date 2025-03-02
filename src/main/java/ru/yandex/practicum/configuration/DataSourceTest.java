package ru.yandex.practicum.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

/**
 * Конфигурация dataSource (H2 in memory DB).
 */
@Component
@Profile("test")
public class DataSourceTest implements DataSourceConfig {
    private final Environment environment;

    @Autowired
    public DataSourceTest(Environment environment) {
        this.environment = environment;
    }

    @Override
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.test.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.test.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.test.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.test.password"));

        return dataSource;
    }
}