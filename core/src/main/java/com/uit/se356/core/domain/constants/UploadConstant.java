package com.uit.se356.core.domain.constants;

// Đưa vào đây các hằng số ít thay đổi liên quan đến việc upload, như loại tệp được phép, kích thước
// tối đa, v.v.
public interface UploadConstant {
  String[] AVATAR_ALLOWED_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp"};
  long AVATAR_MAX_SIZE_IN_BYTES = 5 * 1024 * 1024; // 5MB
}
