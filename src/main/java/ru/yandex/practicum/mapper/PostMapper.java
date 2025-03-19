package ru.yandex.practicum.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.PostDto;

/**
 * Маппер между объектами DAO Post и DTO PostDto.
 */
@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {
  Post toPost(PostDto dto);

  PostDto toPreviewDto(Post post);

  PostDto toDto(Post post);

  default Page<PostDto> toDtoPage(Page<Post> posts) {
    List<PostDto> dtos = posts.stream()
                              .map(this::toPreviewDto)
                              .toList();
    return new PageImpl<>(dtos, pageable(posts), posts.getTotalElements());
  }

  default Pageable pageable(Page<Post> posts) {
    return posts.getPageable();
  }
}