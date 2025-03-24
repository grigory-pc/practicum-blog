package ru.yandex.practicum.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.PostTag;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.exceptions.ImageLoadException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.SaveFileException;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.PostService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostTagRepository postTagRepository;
  private final TagRepository tagRepository;
  private final PostMapper postMapper;

  @Override
  public Page<PostDto> findAllPosts(String search, int pageNumber, int pageSize) {
    Page<Post> posts;

    if (search.isEmpty()) {
      posts = postRepository.findAll(pageNumber, pageSize);
    } else {
      posts = postRepository.findByTags_NameContainingIgnoreCase(search, pageNumber, pageSize);
    }
    Page<PostDto> postDtos = postMapper.toDtoPage(posts);

    postDtos.getContent().forEach(postDto -> postDto.setTags(getTags(postDto)));

    return postDtos;
  }

  @Override
  public PostDto getPostById(Long id) throws NotFoundException {
    Post post = postRepository.findById(id)
                              .orElseThrow(NotFoundException::new);

    PostDto postDto = postMapper.toDto(post);

    postDto.setTags(getTags(postDto));

    return postDto;
  }

  @Override
  @Transactional
  public PostDto savePost(PostDto postDto, String tags, MultipartFile image) {
    Optional<String> imagePath = saveFile(image);
    imagePath.ifPresent(postDto::setImagePath);

    Optional<Post> savedPost = postRepository.save(postMapper.toPost(postDto));

    if (tags != null) {
      updatePostTag(tags, savedPost.get());
    }

    return postMapper.toDto(savedPost.get());
  }

  @Override
  public void deletePostById(Long id) {
    postRepository.deletePostById(id);

    postTagRepository.deleteAllByPostId(id);
  }

  @Override
  public void addLike(Long postId, boolean like) throws NotFoundException {
    if (like) {
      postRepository.increaseLikesCount(postId);
    } else {
      postRepository.decreaseLikesCount(postId);
    }
  }

  @Override
  public byte[] getPostImage(Long postId) {
    try {
      Post post = postRepository.findById(postId)
                                .orElseThrow(NotFoundException::new);

      String filePath = post.getImagePath();

      return Files.readAllBytes(Paths.get(filePath));

    } catch (IOException e) {
      throw new ImageLoadException("Ошибка при загрузке изображения", e);
    }
  }

  private Optional<String> saveFile(MultipartFile image) throws SaveFileException {
    if (image != null && !image.getOriginalFilename().isBlank()) {
      try {
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
          Files.createDirectories(uploadDir);
        }

        String filename = UUID.randomUUID() + "-" + image.getOriginalFilename();

        Path filePath = uploadDir.resolve(filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return Optional.of(filePath.toString());
      } catch (IOException e) {
        throw new SaveFileException("Ошибка при сохранении файла", e);
      }
    } else {
      return Optional.empty();
    }
  }

  private void updatePostTag(String tags, Post savedPost) {
    List<String> tagList = getTagsFromString(tags);

    postTagRepository.deleteAllByPostId(savedPost.getId());

    List<PostTag> newPostTags = new ArrayList<>();

    for (String tagName : tagList) {
      Tag tag = tagRepository.findDistinctByTagName(tagName)
                             .orElseGet(() -> tagRepository.save(new Tag(tagName)));

      PostTag postTag = new PostTag();
      postTag.setPostId(savedPost.getId());
      postTag.setTagId(tag.getId());

      newPostTags.add(postTag);
    }

    postTagRepository.saveAll(newPostTags);
  }

  private List<String> getTags(PostDto post) {
    return postTagRepository.findAllByPostId(post.getId())
                            .stream()
                            .map(postTag -> tagRepository.findById(postTag.getTagId())
                                                         .orElseThrow(
                                                             () -> new EntityNotFoundException(
                                                                 "Tag not found"))
                                                         .getTagName())
                            .toList();
  }


  private static List<String> getTagsFromString(String tags) {
    return Arrays.stream(tags.split(","))
                 .map(String::trim)
                 .filter(tag -> !tag.isEmpty())
                 .toList();
  }
}