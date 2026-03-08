package com.uit.se356.core.application.area.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.area.result.WardResult;
import com.uit.se356.core.domain.vo.area.Polygon;
import com.uit.se356.core.domain.vo.area.ProvinceId;
import com.uit.se356.core.domain.vo.area.WardType;
import java.util.ArrayList;
import java.util.List;

public record CreateWardCommand(
    String code, String name, ProvinceId provinceId, WardType type, Polygon polygon)
    implements Command<WardResult> {
  public CreateWardCommand {
    List<FieldError> errors = new ArrayList<>();
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
    if (provinceId == null || provinceId.value().isBlank()) {
      errors.add(
          new FieldError(
              "provinceId",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"provinceId"}));
    }
    if (type == null) {
      errors.add(
          new FieldError(
              "type", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"type"}));
    }
    if (polygon == null) {
      errors.add(
          new FieldError(
              "polygon", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"polygon"}));
    }

    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
