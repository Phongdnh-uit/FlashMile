package com.uit.se356.core.application.upload.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.upload.result.PresignedUrlResult;
import com.uit.se356.core.domain.vo.upload.UploadType;
import java.util.ArrayList;
import java.util.List;

public record UploadPresignedUrlCommand(
    String fileName, String contentType, Long fileSize, UploadType type)
    implements Command<PresignedUrlResult> {
  public UploadPresignedUrlCommand {
    List<FieldError> errors = new ArrayList<>();
    if (fileName == null || fileName.isBlank()) {
      errors.add(
          new FieldError(
              "fileName",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"fileName"}));
    }
    if (contentType == null || contentType.isBlank()) {
      errors.add(
          new FieldError(
              "contentType",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"contentType"}));
    }
    if (fileSize == null || fileSize <= 0) {
      errors.add(
          new FieldError(
              "fileSize",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"fileSize"}));
    }
    if (type == null) {
      errors.add(
          new FieldError(
              "uploadType",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"uploadType"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
