package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PagingDto {
  private int pageNumber;
  private int pageSize;
  private boolean hasPrevious;
  private boolean hasNext;
}
