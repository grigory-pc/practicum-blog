package ru.yandex.practicum.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;

/**
 * Маппер между объектами DAO Post и DTO Post.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class, CommentMapper.class})
public interface PostMapper {
  Post toPost(PostSaveDto dto);

  @Mapping(target = "countComments",
           expression = "java(posts.getComments() != null ? posts.getComments().size() : 0)")
  @Mapping(target = "countLikes", source = "likes")
  @Mapping(target = "postText", source = "text")
  List<PostPreviewDto> toDto(Iterable<Post> posts);

  @Mapping(target = "postText", source = "text")
  PostFullDto toDto (Post post);
}
