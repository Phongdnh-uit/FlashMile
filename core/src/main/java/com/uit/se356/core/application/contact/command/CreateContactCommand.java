package com.uit.se356.core.application.contact.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.exception.ContactErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;

public record CreateContactCommand(
    UserId userId, String name, String phoneNumber, String address, String note)
    implements Command<ContactResult> {
  public CreateContactCommand {
    // BR: Check Mandatory Fields
    if (name == null || name.isBlank() || phoneNumber == null || phoneNumber.isBlank()) {
      throw new AppException(ContactErrorCode.INVALID_MANDATORY_FIELDS);
    }
  }
}
