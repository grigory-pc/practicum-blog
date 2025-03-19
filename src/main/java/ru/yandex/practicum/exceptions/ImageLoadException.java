package ru.yandex.practicum.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@NoArgsConstructor
public class ImageLoadException extends RuntimeException {
  public ImageLoadException(String message, Throwable cause) {
    super(message, cause);
  }
}