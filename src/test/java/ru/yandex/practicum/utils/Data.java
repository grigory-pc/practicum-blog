package ru.yandex.practicum.utils;


import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostDto;

@UtilityClass
public class Data {
  public static final String IMAGE_MAN_PATH = "src/test/resources/man.jpg";
  public static final Long ID_ONE = 1L;
  public static final String TAG = "test";

  public PostDto getPostDto() {
    return PostDto.builder()
                  .id(ID_ONE)
                  .title("test")
                  .text("text")
                  .imagePath(IMAGE_MAN_PATH)
                  .likesCount(0)
                  .comments(new ArrayList<>())
                  .tags(new ArrayList<>())
                  .build();
  }

  public Post getPost() {
    return Post.builder()
               .id(ID_ONE)
               .title("test")
               .imagePath(IMAGE_MAN_PATH)
               .text("text")
               .comments(new ArrayList<>())
               .likesCount(0)
               .build();
  }

  public PostDto getPostSaveDto() {
    return PostDto.builder()
                  .title("test")
                  .text("text")
                  .imagePath(IMAGE_MAN_PATH)
                  .likesCount(0)
                  .tags(List.of("one", "two"))
                  .build();
  }

  public Comment getComment(Long id) {
    return new Comment(id, getPost(), "text");
  }

  public PostTag getPostTag() {
    return new PostTag(1L, 1L);
  }

  public Tag getTag() {
    return new Tag(1L, TAG);
  }

  public MockMultipartFile getImage() {
    return new MockMultipartFile(
        "image",
        "test.jpg",
        "image/jpeg",
        "Test Image Data".getBytes()
    );
  }

  public MockMultipartFile getTitleFile() {

    return new MockMultipartFile(
        "title",
        "",
        "text/plain",
        "test".getBytes()
    );
  }

  public MockMultipartFile getTextFile() {

    return new MockMultipartFile(
        "text",
        "",
        "text/plain",
        "text".getBytes()
    );
  }

  public MockMultipartFile getTagsFile() {

    return new MockMultipartFile(
        "tags",
        "",
        "text/plain",
        "tag".getBytes()
    );
  }
}