package com.uit.se356.core.application.upload.strategies;

import com.uit.se356.core.application.upload.command.UploadPresignedUrlCommand;
import com.uit.se356.core.domain.vo.upload.UploadType;

public class AvatarUploadPolicy implements UploadPolicy {

  @Override
  public boolean validate(UploadPresignedUrlCommand command) {
    // Kiểm tra loại tệp (chỉ cho phép JPEG, PNG, GIF)
    String fileType = command.contentType().toLowerCase();
    if (!fileType.equals("image/jpeg")
        && !fileType.equals("image/png")
        && !fileType.equals("image/gif")) {
      return false;
    }

    // Kiểm tra kích thước tệp (giới hạn 5MB)
    long maxSizeInBytes = 5 * 1024 * 1024; // 5MB
    if (command.fileSize() > maxSizeInBytes) {
      return false;
    }

    return true;
  }

  @Override
  public UploadType type() {
    return UploadType.AVATAR;
  }
}
