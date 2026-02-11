package com.uit.se356.core.application.authentication.port;

import java.time.Duration;
import java.util.Optional;

public interface CacheRepository {
  /**
   * Lưu trữ một giá trị vào cache với thời gian hết hạn (TTL).
   *
   * @param key Khóa định danh (ví dụ: số điện thoại)
   * @param value Giá trị (ví dụ: mã OTP)
   * @param ttl Thời gian sống (ví dụ: 5 phút)
   */
  void set(String key, String value, Duration ttl);

  /** Lưu trữ giá trị không có thời gian hết hạn. */
  void set(String key, String value);

  /**
   * Lấy giá trị từ cache.
   *
   * @param key Khóa định danh
   * @return Optional chứa giá trị nếu tìm thấy
   */
  Optional<String> get(String key);

  /** Xóa một key khỏi cache. */
  void delete(String key);

  /** Kiểm tra xem key có tồn tại trong cache không. */
  boolean exists(String key);

  /** Cập nhật thời gian hết hạn cho một key đã tồn tại. */
  void expire(String key, Duration ttl);
}
