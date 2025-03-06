package ru.yandex.practicum.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.TagService;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  @Override
  public List<TagDto> findAllTags() {
    return tagMapper.toDto(tagRepository.findAll());
  }
}