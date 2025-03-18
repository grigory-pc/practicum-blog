package ru.yandex.practicum.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.TagDto;

@UtilityClass
public class Data {
  private static final String IMAGE_MAN_PATH = "man.jpg";
  public static final Long ID_ONE = 1L;
  public static final Long ID_TWO = 2L;
  public static final String TAG = "test";
  public static final String TAG_TWO = "test_two";

  public PostDto getPostDto() {
    return PostDto.builder()
                  .id(ID_ONE)
                  .title("test")
                  .text("text")
                  .imagePath("image_path")
                  .likesCount(0)
                  .comments(new ArrayList<>())
                  .tags(new ArrayList<>())
                  .build();
  }

  public Post getPost() {
    return Post.builder()
               .id(ID_ONE)
               .title("test")
               .imagePath("image_path")
               .postText("text")
               .comments(new ArrayList<>())
               .likesCount(0)
               .build();
  }

  public PostDto getPostSaveDto() {
    return PostDto.builder()
                  .title("test")
                  .text("text")
                  .imagePath("image_path")
                  .likesCount(0)
                  .tags(List.of("one", "two"))
                  .build();
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

  public Set<Tag> getTags() {
    return Set.of(new Tag(1L, TAG), new Tag(2L, TAG_TWO));
  }

  public Set<TagDto> getTagDtos() {
    return Set.of(new TagDto(1L, TAG), new TagDto(2L, TAG_TWO));
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
