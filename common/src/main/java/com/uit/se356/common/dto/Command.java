package com.uit.se356.common.dto;

/**
 * Interface đại diện cho một lệnh (command) trong mô hình Command Pattern. Lệnh này có thể được gửi
 * đến một CommandBus để thực thi và trả về kết quả.
 *
 * @param R Kết quả trả về
 */
public interface Command<R> {}
