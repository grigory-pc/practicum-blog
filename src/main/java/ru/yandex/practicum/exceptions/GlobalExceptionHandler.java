package ru.yandex.practicum.exceptions;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * Обработка исключений.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final String ERROR = "error";
  private static final String NOT_FOUND = "Not_Found";
  private static final String INTERNAL_ERROR = "Internal_Error";

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleBadMetadataFormatException(NotFoundException exception) {
    log.error(exception.getMessage());

    return Map.of(ERROR, NOT_FOUND);
  }

  @ExceptionHandler(MultipartException.class)
  public void handleMultipartException(MultipartException ex) {
    log.error("Ошибка загрузки файла: {}", ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, String> handleException(Exception exception) {
    log.error(exception.getMessage());

    return Map.of(ERROR, INTERNAL_ERROR);
  }

}