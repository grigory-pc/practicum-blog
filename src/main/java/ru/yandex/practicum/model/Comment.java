package ru.yandex.practicum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Класс комментария.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Comment {
    private int id;
    private int postId;
    private String text;
}
