package ru.yandex.practicum.view.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.TagDto;

@UtilityClass
public class Data {
  private static final String IMAGE_MAN_PATH = "C:\\Users\\Data\\Desktop\\man.jpg";
  private static final String IMAGE_WOMAN_PATH = "C:\\Users\\Data\\Desktop\\woman.jpg";

  public List<PostPreviewDto> getPosts() {

    byte[] imageManBytes = getImageBytes(IMAGE_MAN_PATH);
    byte[] imageWomanBytes = getImageBytes(IMAGE_WOMAN_PATH);

    return Arrays.asList(
        createPost(1L, imageManBytes, "John Smith",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   1000, 300, Set.of(new TagDto(1L, "test"), new TagDto(2L, "2024"))),
        createPost(2L, imageWomanBytes, "Abagail Libbie",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   50, 400, Set.of(new TagDto(1L, "test"), new TagDto(4L, "2025"),
                                   new TagDto(3L, "practicum"))),
        createPost(3L, imageManBytes, "Alberto Raya",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   1020, 5, Set.of(new TagDto(5L, "practicum"))),
        createPost(4L, imageWomanBytes, "Emmy Elsner",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   100, 100, Set.of(new TagDto(2L, "2024"))),
        createPost(5L, imageManBytes, "Alf Huncoot",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   1000, 138, Set.of(new TagDto(1L, "test")))
    );
  }

  public PostFullDto getPostFullDto(Long postId) {
    byte[] imageManBytes = getImageBytes(IMAGE_MAN_PATH);
    byte[] imageWomanBytes = getImageBytes(IMAGE_WOMAN_PATH);

    List<PostFullDto> posts = List.of(new PostFullDto(1L, "John Smith", imageManBytes,
                                                      "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                                                      Set.of(new TagDto(1L, "test"),
                                                             new TagDto(2L, "2024")),
                                                      List.of(new CommentDto(1L, "comment1"),
                                                              new CommentDto(2L, "comment2"))),
                                      new PostFullDto(2L, "Abagail Libbie", imageWomanBytes,
                                                      "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                                                      Set.of(new TagDto(1L, "test"),
                                                             new TagDto(4L, "2025")),
                                                      List.of(new CommentDto(4L, "comment4"),
                                                              new CommentDto(3L, "comment3"))));


    return posts.stream()
                .filter(post -> post.id().equals(postId))
                .findFirst()
                .orElse(new PostFullDto(3L, "Alberto Raya", imageManBytes,
                                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                                        Set.of(new TagDto(1L, "test"),
                                               new TagDto(2L, "2024")),
                                        List.of(new CommentDto(1L, "comment1"),
                                                new CommentDto(2L, "comment2"))));
  }

  private static PostPreviewDto createPost(Long id, byte[] image, String title, String postText,
                                           Integer likes, Integer comments, Set<TagDto> tags) {

    return new PostPreviewDto(id, title, image, postText, likes, comments, tags);
  }

  private static byte[] getImageBytes(String path) {
    File fileImage = new File(path);

    try {
      return Files.readAllBytes(fileImage.toPath());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String truncateText(String text, int maxLines) {
    String[] lines = text.split("\n");
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
      result.append(lines[i]).append("\n");
    }

    return result.toString().trim();
  }
}