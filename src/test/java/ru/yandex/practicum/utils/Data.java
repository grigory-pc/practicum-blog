package ru.yandex.practicum.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;

@UtilityClass
public class Data {
  private static final String IMAGE_MAN_PATH = "man.jpg";
  public static final Long ID_ONE = 1L;
  public static final Long ID_TWO = 2L;
  public static final String TAG = "test";

  public PostPreviewDto getPostPreviewDto() {
    return new PostPreviewDto(ID_ONE, "test", getImageBytes(IMAGE_MAN_PATH), "text", 0, 0,
                              new HashSet<>());
  }

  public PostFullDto getPostFullDto() {
    return new PostFullDto(ID_ONE, "test", getImageBytes(IMAGE_MAN_PATH), "text",
                           new HashSet<>(), new ArrayList<>());
  }

  public Post getPost() {
    return new Post(ID_ONE, "test", getImageBytes(IMAGE_MAN_PATH), "text",
                    new ArrayList<>(), 0, new HashSet<>());
  }

  public PostSaveDto getPostSaveDto() {
    return new PostSaveDto("test", getImageBytes(IMAGE_MAN_PATH), "text",
                           Set.of(ID_ONE, ID_TWO));
  }

  public CommentDto getCommentDto(Long id) {
    return new CommentDto(id, "text");
  }

  public Comment getComment(Long id) {
    return new Comment(id, getPost(), "text");
  }

  public TagDto getTagDto() {
    return new TagDto(ID_ONE, TAG);
  }

  public PostTag getPostTag() {
    return new PostTag(1L, 1L);
  }

  public Tag getTag() {
    return new Tag(1L, TAG);
  }

  private static byte[] getImageBytes(String path) {
    File fileImage = new File(path);

    try {
      return Files.readAllBytes(fileImage.toPath());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
