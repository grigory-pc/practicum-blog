package ru.yandex.practicum.dao;

import java.io.Serializable;
import lombok.Data;

@Data
public class PostTagKey implements Serializable {
    private Long postId;
    private Long tagId;
}
