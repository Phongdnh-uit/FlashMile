package com.uit.se356.common.exception;

public interface ErrorCode {
  abstract String getCode(); // Mã lỗi duy nhất

  abstract String getMessageKey(); // Khóa để tra cứu thông điệp lỗi

  abstract int getHttpStatus(); // Mã trạng thái HTTP tương ứng
}
