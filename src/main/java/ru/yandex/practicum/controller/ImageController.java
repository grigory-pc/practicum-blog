package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.service.PostService;

/**
 * Контроллер обрабатывает запросы /image.
 */
@Slf4j
@Controller
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
  private final PostService postService;

  /**
   * Получение байтового массива для картинки.
   *
   * @param postId - id поста.
   * @return байтовый массив для картинки.
   */
  @GetMapping("/{postId}")
  public ResponseEntity<byte[]> getPostImage(@PathVariable Long postId) {
    try {
      byte[] imageBytes = postService.getPostImage(postId);

      if (imageBytes == null) {
        return ResponseEntity.notFound().build();
      }

      String contentType = "image/jpeg";

      return ResponseEntity.ok()
                           .contentType(MediaType.parseMediaType(contentType))
                           .body(imageBytes);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}