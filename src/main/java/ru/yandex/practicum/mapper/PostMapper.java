package ru.yandex.practicum.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;

/**
 * Маппер между объектами DAO Post и DTO Post.
 */
@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface PostMapper {
  Post toPost(PostSaveDto dto);

  @Mapping(target = "countComments",
           expression = "java(posts.getComments() != null ? posts.getComments().size() : 0)")
  @Mapping(target = "countLikes", source = "like.likesCount")
  @Mapping(target = "postText", source = "text")
  @Mapping(target = "tagIds", source = "tags")
  List<PostPreviewDto> toFullDto(Iterable<Post> posts);

  @Mapping(target = "postText", source = "text")
  @Mapping(target = "tagIds", expression = "java(mapTagsToIds(post.getTags()))")
  PostFullDto toFullDto(Post post);

  default Set<Long> mapTagsToIds(Set<Tag> tags) {
    if (tags == null) {
      return new HashSet<>();
    }
    return tags.stream()
               .map(Tag::getId)
               .collect(Collectors.toSet());
  }
}
