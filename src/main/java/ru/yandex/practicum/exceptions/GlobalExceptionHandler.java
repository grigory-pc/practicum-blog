package ru.yandex.practicum.exceptions;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  private final static String ERROR = "error";
  private final static String NOT_FOUND = "Not_Found";
  private final static String INTERNAL_ERROR = "Internal_Error";

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleBadMetadataFormatException(NotFoundException exception) {
    log.error(exception.getMessage());

    return Map.of(ERROR, NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, String> handleException(Exception exception) {
    log.error(exception.getMessage());

    return Map.of(ERROR, INTERNAL_ERROR);
  }
}