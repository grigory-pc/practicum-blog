package ru.yandex.practicum.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;

/**
 * Маппер между объектами DAO Post и DTO Post.
 */
@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface PostMapper {
  Post toPost(PostSaveDto dto);

  @Mapping(target = "count_comments",
           expression = "java(posts.getComments() != null ? posts.getComments().size() : 0)")
  @Mapping(target = "count_likes", source = "likes")
  List<PostPreviewDto> toDto(Iterable<Post> posts);
}
