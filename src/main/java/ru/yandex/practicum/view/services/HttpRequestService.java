package ru.yandex.practicum.view.services;

import com.vaadin.flow.component.notification.Notification;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;

@Service
public class HttpRequestService {
//  @Autowired
//  private RestTemplate restTemplate;

  public boolean sendPostToServer(PostSaveDto postDto) {
    System.out.println("Request sended");

    return true;
//    try {
//      HttpHeaders headers = new HttpHeaders();
//      headers.setContentType(MediaType.APPLICATION_JSON);
//
//      HttpEntity<PostSaveDto> requestEntity = new HttpEntity<>(postDto, headers);
//      ResponseEntity<String> response = restTemplate.postForEntity(
//          "http://localhost:8080/posts",
//          requestEntity,
//          String.class
//      );
//
//      if (response.getStatusCode().is2xxSuccessful()) {
//        Notification.show("Пост успешно создан");
//        return true;
//      } else {
//        return false;
//      }
//    } catch (Exception e) {
//      return false;
//    }
  }

  public Set<TagDto> loadTagsFromBackend() {
    return Set.of(new TagDto(1L, "test"), new TagDto(2L, "2024"), new TagDto(3L, "practicum"),
                  new TagDto(4L, "2025"), new TagDto(5L, "practicum"));
    //    try {
    //      ResponseEntity<Set<TagDto>> response = restTemplate.exchange(
    //          "http://localhost:8080/posts/tags",
    //          HttpMethod.GET,
    //          null,
    //          new ParameterizedTypeReference<>() {
    //          }
    //      );
    //
    //      if (response.getStatusCode().is2xxSuccessful()) {
    //        return response.getBody();
    //      } else {
    //        return new HashSet<>();
    //      }
    //    } catch (RestClientException e) {
    //      return new HashSet<>();
    //    }
  }
}