package ru.yandex.practicum.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Конфигурация для web.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.yandex.practicum")
@PropertySource("classpath:application-test.yml")
public class WebConfiguration {}
