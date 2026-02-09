package com.uit.se356.common.services;

/**
 * Tiện ích cho xử lý các lệnh.
 *
 * @param <C> Kiểu lệnh
 * @param <R> Kiểu kết quả trả về
 */
public interface CommandHandler<C, R> {
  R handle(C command);
}
