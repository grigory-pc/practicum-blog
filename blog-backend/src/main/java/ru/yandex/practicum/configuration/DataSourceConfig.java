package ru.yandex.practicum.configuration;

import javax.sql.DataSource;

public interface DataSourceConfig {
    DataSource getDataSource();
}