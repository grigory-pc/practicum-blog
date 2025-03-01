package ru.yandex.practicum.controller.mvc.test.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Конфигурация для web.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.yandex.practicum.mvc.test")
@PropertySource("classpath:test-application.properties")
public class WebConfiguration {}
