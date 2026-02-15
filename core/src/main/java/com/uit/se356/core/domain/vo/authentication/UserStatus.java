package com.uit.se356.core.domain.vo.authentication;

public enum UserStatus {
  UNVERIFIED, // chưa xác thực email hoặc số điện thoại
  ACTIVE, // đang hoạt động
  BLOCKED, // bị khóa
  DELETED // đã xóa
}
