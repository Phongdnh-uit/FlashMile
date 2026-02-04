package com.uit.se356.common.dto;

import java.util.Objects;

public record FieldError(String field, String message) {
  public FieldError {
    // Tên trường và thông điệp không được null
    Objects.requireNonNull(field);
    Objects.requireNonNull(message);
  }
}
