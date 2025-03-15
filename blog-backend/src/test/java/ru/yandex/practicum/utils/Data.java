package ru.yandex.practicum.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Like;
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
    return new PostPreviewDto(ID_ONE, "test", getImageBytes(), "text", 2, 1,
                              Set.of(getTagDto()));
  }

  public PostFullDto getPostFullDto() {
    return new PostFullDto(ID_ONE, "test", getImageBytes(), "text",
                           Set.of(getTagDto()), List.of(getCommentDto(ID_ONE)));
  }

  public Post getPost() {
    return new Post(ID_ONE, "test", getImageBytes(), "text",
                    new HashSet<>(), getLike(ID_ONE), new HashSet<>());
  }

  public PostSaveDto getPostSaveDto() {
    return new PostSaveDto("test", getImageBytes(), "text",
                           Set.of(ID_ONE, ID_TWO));
  }

  public CommentDto getCommentDto(Long id) {
    return new CommentDto(id, "text");
  }

  public Comment getComment(Long id) {
    return new Comment(id, getPost(), "text");
  }

  public Like getLike(Long postId) {
    return new Like(ID_ONE, postId, 1);
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

  private static byte[] getImageBytes() {
    try {
      URL resource = Data.class.getClassLoader().getResource(IMAGE_MAN_PATH);
    Path path = Paths.get(resource.toURI());
    File fileImage = new File(String.valueOf(path));


      return Files.readAllBytes(fileImage.toPath());

    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
