package com.uit.se356.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

// @RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    Instant timestamp,
    String path, // Đường dẫn yêu cầu
    int status, // Mã trạng thái HTTP
    String message, // Message lỗi thân thiện với người dùng
    List<FieldError> fieldErrors, // Danh sách trường lỗi chi tiết trong xác thực
    String errorCode) { // Mã lỗi nghiệp vụ -> Có thể chuyển sang enum

  public ErrorResponse(
      String path, int status, String message, List<FieldError> fieldErrors, String errorCode) {
    this(Instant.now(), path, status, message, fieldErrors, errorCode);
    Objects.requireNonNull(path);
    Objects.requireNonNull(errorCode);
  }
}
