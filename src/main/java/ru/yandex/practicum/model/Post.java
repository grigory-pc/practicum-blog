package ru.yandex.practicum.model;

import lombok.*;

import java.util.List;

/**
 * Класс поста.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Post {
    private int id;
    private String title;
    private String image;
    private String text;
    private List<Comment> comments;
    private int likes;
    private List<String> tags;
}
