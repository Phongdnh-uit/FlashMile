package com.uit.se356.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

// @RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    Instant timestamp,
    String path, // Đường dẫn yêu cầu
    int status, // Mã trạng thái HTTP
    String message, // Message lỗi thân thiện với người dùng
    Map<String, String> errors, // Danh sách trường lỗi chi tiết trong xác thực
    String errorCode) { // Mã lỗi nghiệp vụ -> Có thể chuyển sang enum

  public ErrorResponse(
      String path, int status, String message, Map<String, String> errors, String errorCode) {
    this(Instant.now(), path, status, message, errors, errorCode);
    Objects.requireNonNull(path);
    Objects.requireNonNull(errorCode);
  }
}
