package com.uit.se356.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    Instant timestamp, // Thời gian phản hồi
    int status, // Mã trạng thái HTTP
    String message, // Thông điệp thân thiện với người dùng
    T data // Dữ liệu phản hồi
    ) {
  public ApiResponse(int status, String message, T data) {
    this(Instant.now(), status, message, data);
    Objects.requireNonNull(message);
  }

  public static <T> ApiResponse<T> ok(T data, String message) {
    return new ApiResponse<>(200, message, data);
  }

  public static <T> ApiResponse<T> created(T data, String message) {
    return new ApiResponse<>(201, message, data);
  }

  public static <T> ApiResponse<Void> noContent(String message) {
    return new ApiResponse<>(204, message, null);
  }
}
