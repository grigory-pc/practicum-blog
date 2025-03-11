package ru.yandex.practicum.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Like;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;

@UtilityClass
public class Data {
  public static final Long ID_ONE = 1L;
  public static final Long ID_TWO = 2L;
  ObjectMapper objectMapper = new ObjectMapper();

  public PostPreviewDto getPostPreviewDto() throws JsonProcessingException {
    return new PostPreviewDto(ID_ONE, "test", objectMapper.writeValueAsBytes("test"), "text", 2, 1,
                              Set.of(ID_ONE, ID_TWO));
  }

  public PostFullDto getPostFullDto() throws JsonProcessingException {
    return new PostFullDto(ID_ONE, "test", objectMapper.writeValueAsBytes("test"), "text",
                           Set.of(ID_ONE, ID_TWO), List.of(getCommentDto(ID_ONE)));
  }

  public Post getPost() throws JsonProcessingException {
    return new Post(ID_ONE, "test", objectMapper.writeValueAsBytes("test"), "text",
                    new HashSet<>(), getLike(ID_ONE), new HashSet<>());
  }

  public PostSaveDto getPostSaveDto() throws JsonProcessingException {
    return new PostSaveDto("test", objectMapper.writeValueAsBytes("test"), "text",
                           Set.of(ID_ONE, ID_TWO));
  }

  public CommentDto getCommentDto(Long id) {
    return new CommentDto(id, "text");
  }

  public Comment getComment(Long id) throws JsonProcessingException {
    return new Comment(id, getPost(), "text");
  }

  public Like getLike(Long postId) {
    return new Like(ID_ONE, postId, 1);
  }

}
