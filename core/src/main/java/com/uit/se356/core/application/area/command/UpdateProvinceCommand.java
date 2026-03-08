package com.uit.se356.core.application.area.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.area.result.ProvinceResult;
import com.uit.se356.core.domain.vo.area.ProvinceId;
import com.uit.se356.core.domain.vo.area.ProvinceType;
import java.util.ArrayList;
import java.util.List;

public record UpdateProvinceCommand(ProvinceId id, String code, String name, ProvinceType type)
    implements Command<ProvinceResult> {
  public UpdateProvinceCommand {
    List<FieldError> errors = new ArrayList<>();
    if (id == null || id.value().isBlank()) {
      errors.add(
          new FieldError(
              "id", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"id"}));
    }
    if (code == null || code.isBlank()) {
      errors.add(
          new FieldError(
              "code", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"code"}));
    }
    if (name == null || name.isBlank()) {
      errors.add(
          new FieldError(
              "name", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"name"}));
    }
    if (type == null) {
      errors.add(
          new FieldError(
              "type", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"type"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
