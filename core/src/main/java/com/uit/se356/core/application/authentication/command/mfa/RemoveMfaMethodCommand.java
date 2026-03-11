package com.uit.se356.core.application.authentication.command.mfa;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import java.util.ArrayList;
import java.util.List;

public record RemoveMfaMethodCommand(MfaMethod method) implements Command<Void> {
  public RemoveMfaMethodCommand {
    List<FieldError> errors = new ArrayList<>();
    if (method == null) {
      errors.add(
          new FieldError(
              "method", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"method"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
