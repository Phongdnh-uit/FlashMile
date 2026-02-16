package com.uit.se356.common.exception;

public interface ErrorCode {
  String getCode(); // Mã lỗi duy nhất

  String getMessageKey(); // Khóa để tra cứu thông điệp lỗi

  int getHttpStatus(); // Mã trạng thái HTTP tương ứng
}
