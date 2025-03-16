package ru.yandex.practicum.view.utility;

import java.util.Set;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;

@UtilityClass
public class RestService {
//  @Autowired
//  private RestTemplate restTemplate;

  public boolean sendPostToServer(PostSaveDto postDto) {
    System.out.println("Request sended: " + postDto.title());

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

  public PostFullDto getPostFullDto(Long postId){
    return Data.getPostFullDto(postId);
  }
}