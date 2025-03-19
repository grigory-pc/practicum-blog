package ru.yandex.practicum.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение в случае, если объект не был найден в БД.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
@NoArgsConstructor
public class NotFoundException extends RuntimeException {
}