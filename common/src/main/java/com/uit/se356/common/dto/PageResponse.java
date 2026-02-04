package com.uit.se356.common.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean last,
    boolean first) {
  public static <T> PageResponse<T> empty() {
    return new PageResponse<>(List.of(), 0, 0, 0, 0, true, true);
  }

  public static <T> PageResponse<T> from(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isLast(),
        page.isFirst());
  }

  public static <T, R> PageResponse<R> from(Page<T> page, Function<? super T, ? extends R> mapper) {
    List<R> mappedContent = page.getContent().stream().map(mapper).collect(Collectors.toList());
    return new PageResponse<>(
        mappedContent,
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isLast(),
        page.isFirst());
  }
}
