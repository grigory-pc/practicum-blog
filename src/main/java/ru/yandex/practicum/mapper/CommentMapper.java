package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dto.CommentDto;

/**
 * Маппер между объектами DAO Comment и DTO CommentDto.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

  CommentDto toDto(Comment comment);

  Comment toComment(CommentDto commentDto);
}