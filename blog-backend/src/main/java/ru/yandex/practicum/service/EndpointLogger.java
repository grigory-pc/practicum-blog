package ru.yandex.practicum.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointLogger implements ApplicationListener<ContextRefreshedEvent> {
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    ApplicationContext context = event.getApplicationContext();
    context.getBean(RequestMappingHandlerMapping.class)
           .getHandlerMethods().forEach((k, v) -> {
             System.out.println("Endpoint: " + k.getPatternsCondition());
           });
  }
}