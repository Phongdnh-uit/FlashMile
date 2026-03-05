package com.uit.se356.core.application.area.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.area.result.ProvinceResult;
import com.uit.se356.core.domain.vo.area.BoundingBox;
import java.util.ArrayList;
import java.util.List;

public record UpdateProvinceCommand(String id, String code, String name, BoundingBox boundingBox)
    implements Command<ProvinceResult> {
  public UpdateProvinceCommand {
    List<FieldError> errors = new ArrayList<>();
    if (id == null || id.isBlank()) {
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
    if (boundingBox == null) {
      errors.add(
          new FieldError(
              "boundingBox",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"boundingBox"}));
    }
    if (!errors.isEmpty()) {
      throw new com.uit.se356.common.exception.AppException(
          CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
