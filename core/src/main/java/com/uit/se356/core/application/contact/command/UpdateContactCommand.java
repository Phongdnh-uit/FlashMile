package com.uit.se356.core.application.contact.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.vo.area.ContactId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.ArrayList;
import java.util.List;

public record UpdateContactCommand(
    ContactId contactId,
    UserId userId,
    String name,
    String phoneNumber,
    String address,
    String note)
    implements Command<ContactResult> {
  public UpdateContactCommand {
    List<FieldError> errors = new ArrayList<>();
    if (contactId == null || contactId.value().isBlank()) {
      errors.add(
          new FieldError(
              "contactId",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"contactId"}));
    }
    if (userId == null || userId.value().isBlank()) {
      errors.add(
          new FieldError(
              "userId", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"userId"}));
    }
    if (name == null || name.isBlank()) {
      errors.add(
          new FieldError(
              "name", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"name"}));
    }
    if (phoneNumber == null || phoneNumber.isBlank()) {
      errors.add(
          new FieldError(
              "phoneNumber",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"phoneNumber"}));
    }

    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
