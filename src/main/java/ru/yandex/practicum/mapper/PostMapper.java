package ru.yandex.practicum.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
           expression = "java(post.getComments() != null ? post.getComments().size() : 0)")
  @Mapping(target = "countLikes", source = "likes.countLikes")
  @Mapping(target = "postText", source = "text")
  PostPreviewDto toPreviewDto(Post post);

  @Mapping(target = "postText", source = "text")
  PostFullDto toFullDto(Post post);

  default Page<PostPreviewDto> toDtoPage(Page<Post> posts) {
    List<PostPreviewDto> dtos = posts.stream()
                                     .map(this::toPreviewDto)
                                     .toList();
    return new PageImpl<>(dtos, pageable(posts), posts.getTotalElements());
  }

  default Pageable pageable(Page<Post> posts) {
    return posts.getPageable();
  }
}