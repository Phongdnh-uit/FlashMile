package com.uit.se356.core.domain.vo.upload;

public enum FileStatus {
  PENDING,
  UPLOADED,
  INVALID, // Trạng thái khi file không hợp lệ, có thể do kích thước hoặc content type không đúng
  DELETED // Trạng thái khi xóa nhưng vẫn giữ lại để xóa theo lịch trình
}
