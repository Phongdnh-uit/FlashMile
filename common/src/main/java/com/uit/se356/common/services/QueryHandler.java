package com.uit.se356.common.services;

/**
 * Tiện ích để xử lý các truy vấn.
 *
 * @param <Q> Kiểu truy vấn
 * @param <R> Kiểu kết quả trả về
 */
public interface QueryHandler<Q, R> {
  R handle(Q query);
}
