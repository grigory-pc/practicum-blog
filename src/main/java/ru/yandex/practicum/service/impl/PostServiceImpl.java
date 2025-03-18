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
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public Page<PostDto> findAllPosts(String search, Pageable pageable) {
    if (search.isEmpty()) {
      Page<Post> posts = postRepository.findAll(pageable);

      return postMapper.toDtoPage(posts);
    } else {
      Page<Post> posts = postRepository.findByTags_NameContainingIgnoreCase(search, pageable);

      return postMapper.toDtoPage(posts);
    }
  }

  @Override
  public PostDto getPostById(Long id) throws NotFoundException {
    Post post = postRepository.findById(id)
                              .orElseThrow(NotFoundException::new);

    PostDto postDto = postMapper.toDto(post);

    postDto.setTags(postTagRepository.findAllByPostId(post.getId())
                                     .stream()
                                     .map(postTag -> tagRepository.findById(postTag.getTagId())
                                                                  .orElseThrow(
                                                                      () -> new EntityNotFoundException(
                                                                          "Tag not found"))
                                                                  .getTagName())
                                     .collect(Collectors.toList()));

    return postDto;
  }

  @Override
  @Transactional
  public PostDto savePost(PostDto postDto, String tags, MultipartFile image) {
    List<String> tagList = Arrays.stream(tags.split(","))
                                 .map(String::trim)
                                 .filter(tag -> !tag.isEmpty())
                                 .toList();

    postDto.setTags(tagList);

    if (image != null) {
      String imagePath = saveFile(image);
      postDto.setImagePath(imagePath);
    }

    Post savedPost = postRepository.save(postMapper.toPost(postDto));

    updatePostTag(postDto, savedPost);

    return postMapper.toDto(savedPost);
  }

  @Override
  public void deletePostById(Long id) {
    postRepository.deleteById(id);

    postTagRepository.deleteAllByPostId(id);
  }

  @Override
  public void addLike(Long postId, boolean like) throws NotFoundException {
    if (like) {
      postRepository.increaseLikesCount(postId);
    }
    postRepository.decreaseLikesCount(postId);
  }

  @Override
  public byte[] getPostImage(Long postId) {
    try {
      Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new NotFoundException());

      String filePath = post.getImagePath();

      return Files.readAllBytes(Paths.get(filePath));

    } catch (IOException e) {
      throw new ImageLoadException("Ошибка при загрузке изображения", e);
    }
  }

  private String saveFile(MultipartFile multipartFile) throws SaveFileException {
    try {
      Path uploadDir = Paths.get("uploads");
      if (!Files.exists(uploadDir)) {
        Files.createDirectories(uploadDir);
      }

      String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

      Path filePath = uploadDir.resolve(filename);
      Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

      return filePath.toString();
    } catch (IOException e) {
      throw new SaveFileException("Ошибка при сохранении файла", e);
    }
  }

  private void updatePostTag(PostDto postDto, Post savedPost) {
    postTagRepository.deleteAllByPostId(savedPost.getId());

    List<PostTag> newPostTags = new ArrayList<>();

    for (String tagName : postDto.getTags()) {
      Tag tag = tagRepository.findDistinctByTagName(tagName)
                             .orElseGet(() -> tagRepository.save(new Tag(tagName)));

      PostTag postTag = new PostTag();
      postTag.setPostId(savedPost.getId());
      postTag.setTagId(tag.getId());

      newPostTags.add(postTag);
    }

    postTagRepository.saveAll(newPostTags);
  }
}