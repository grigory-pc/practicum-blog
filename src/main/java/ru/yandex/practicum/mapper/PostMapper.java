package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.PostSaveDto;

/**
 * Маппер между объектами Post и DTO Post.
 */
@Mapper(componentModel = "spring")
public interface PostMapper {
  Post toPost(PostSaveDto dto);
}
