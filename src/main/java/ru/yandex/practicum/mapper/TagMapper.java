package ru.yandex.practicum.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.TagDto;

/**
 * Маппер между объектами DAO Tag и DTO Tag.
 */
@Mapper(componentModel = "spring")
public interface TagMapper {
  List<TagDto> toDto(Iterable<Tag> tags);
  default Set<Long> mapTagToTagIds(Set<Tag> tags) {
    return tags.stream()
               .map(Tag::getId)
               .collect(Collectors.toSet());
  }
}