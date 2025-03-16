package ru.yandex.practicum.view.utility;

import com.vaadin.flow.component.notification.Notification;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;

@UtilityClass
public class RestService {
  private static final String BASE_URL = "http://localhost:8080/api/posts";

  private RestTemplate restTemplate;

  public boolean sendPostToServer(PostSaveDto postDto) {
    //ToDo закоментированно до решения проблемы с одновременной работой двух сервлетов и доступности
    // REST-контроллеров.

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

    //Загрушка
    return true;
  }

  public Set<TagDto> loadTagsFromBackend() {
    //ToDo закоментированно до решения проблемы с одновременной работой двух сервлетов и доступности
    // REST-контроллеров.

    //    try {
    //      ResponseEntity<Set<TagDto>> response = restTemplate.exchange(
    //          BASE_URL + "/tags",
    //          HttpMethod.GET,
    //          null,
    //          new ParameterizedTypeReference<>() {
    //          }
    //      );
    //      if (response.getStatusCode().is2xxSuccessful()) {
    //        return response.getBody();
    //      } else {
    //        return new HashSet<>();
    //      }
    //    } catch (Exception e) {
    //      return new HashSet<>();
    //    }

    //Загрушка
    return Data.getTags();
  }

  public Optional<PostFullDto> getPostFullDto(Long postId) {
    //ToDo закоментированно до решения проблемы с одновременной работой двух сервлетов и доступности
    // REST-контроллеров.

    //    try {
    //      ResponseEntity<PostFullDto> response = restTemplate.exchange(
    //          BASE_URL + "/" + postId,
    //          HttpMethod.GET,
    //          null,
    //          new ParameterizedTypeReference<>() {
    //          }
    //      );
    //      if (response.getStatusCode().is2xxSuccessful()) {
    //        return Optional.of(response.getBody());
    //      } else {
    //        return Optional.empty();
    //      }
    //    } catch (Exception e) {
    //      return Optional.empty();
    //    }

    //Загрушка
    return Optional.of(Data.getPostFullDto(postId));
  }

  public boolean deletePost(Long postId) {
    //ToDo закоментированно до решения проблемы с одновременной работой двух сервлетов и доступности
    // REST-контроллеров.

    //    try {
    //      restTemplate.delete(BASE_URL + "/" + postId);
    //      return true;
    //    } catch (Exception e) {
    //      return false;
    //    }

    //Загрушка
    return true;
  }

  public boolean deleteComment(Long commentId) {
    //ToDo закоментированно до решения проблемы с одновременной работой двух сервлетов и доступности
    // REST-контроллеров.

    //    try {
    //      restTemplate.delete(BASE_URL + "/comment/" + commentId);
    //      return true;
    //    } catch (Exception e) {
    //      return false;
    //    }

    //Загрушка
    return true;
  }

  public boolean sendCommentToServer(Long postId,CommentDto commentDto) {
    //ToDo закоментированно до решения проблемы с одновременной работой двух сервлетов и доступности
    // REST-контроллеров.

//    try {
//      HttpHeaders headers = new HttpHeaders();
//      headers.setContentType(MediaType.APPLICATION_JSON);
//
//      HttpEntity<CommentDto> requestEntity = new HttpEntity<>(commentDto, headers);
//      ResponseEntity<String> response = restTemplate.postForEntity(
//          BASE_URL + "/" + postId + "/comment",
//          requestEntity,
//          String.class
//      );
//
//      if (response.getStatusCode().is2xxSuccessful()) {
//        Notification.show("Комментарий успешно создан");
//        return true;
//      } else {
//        return false;
//      }
//    } catch (Exception e) {
//      return false;
//    }

    //Загрушка
    return true;
  }
}