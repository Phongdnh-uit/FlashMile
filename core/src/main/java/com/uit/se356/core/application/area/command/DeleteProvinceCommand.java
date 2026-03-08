package com.uit.se356.core.application.area.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.domain.vo.area.ProvinceId;

public record DeleteProvinceCommand(ProvinceId id) implements Command<Void> {
  public DeleteProvinceCommand {
    if (id == null || id.value().isBlank()) {
      throw new AppException(
          CommonErrorCode.FIELD_REQUIRED,
          new FieldError(
              "id", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"id"}));
    }
  }
}
