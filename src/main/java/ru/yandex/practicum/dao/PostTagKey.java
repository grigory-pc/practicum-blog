package ru.yandex.practicum.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * Класс составного ключа Post Tag.
 */
@Data
public class PostTagKey implements Serializable {
    private Long postId;
    private Long tagId;
}
