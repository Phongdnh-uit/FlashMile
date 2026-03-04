package com.uit.se356.core.application.upload.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import java.util.ArrayList;
import java.util.List;

public record ConfirmUploadCommand(String storageKey) implements Command<Void> {
  public ConfirmUploadCommand {
    List<FieldError> errors = new ArrayList<>();
    if (storageKey == null || storageKey.isBlank()) {
      errors.add(
          new FieldError(
              "storageKey",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"storageKey"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
