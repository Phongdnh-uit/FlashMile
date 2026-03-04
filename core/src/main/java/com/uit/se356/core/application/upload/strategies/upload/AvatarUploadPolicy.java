package com.uit.se356.core.application.upload.strategies.upload;

import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.upload.command.UploadPresignedUrlCommand;
import com.uit.se356.core.domain.constants.UploadConstant;
import com.uit.se356.core.domain.exception.UploadErrorCode;
import com.uit.se356.core.domain.vo.upload.UploadType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvatarUploadPolicy implements UploadPolicy {

  @Override
  public void validate(UploadPresignedUrlCommand command) {
    List<FieldError> errors = new ArrayList<>();
    // Kiểm tra loại tệp (chỉ cho phép JPEG, PNG, GIF)
    String fileType = command.contentType().toLowerCase();
    if (Arrays.asList(UploadConstant.AVATAR_ALLOWED_TYPES).stream().noneMatch(fileType::equals)) {
      errors.add(
          new FieldError(
              "contentType",
              UploadErrorCode.INVALID_FILE_TYPE.getMessageKey(),
              new Object[] {UploadConstant.AVATAR_ALLOWED_TYPES}));
    }

    // Kiểm tra kích thước tệp
    if (command.fileSize() > UploadConstant.AVATAR_MAX_SIZE_IN_BYTES) {
      errors.add(
          new FieldError(
              "fileSize",
              UploadErrorCode.FILE_TOO_LARGE.getMessageKey(),
              new Object[] {UploadConstant.AVATAR_MAX_SIZE_IN_BYTES}));
    }

    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }

  @Override
  public UploadType type() {
    return UploadType.AVATAR;
  }
}
