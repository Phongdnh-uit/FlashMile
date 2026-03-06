package com.uit.se356.core.application.contact.query;

import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.exception.UserErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;

public record GetContactByPhoneQuery(UserId userId, String phoneNumber)
    implements Query<ContactResult> {
  public GetContactByPhoneQuery {
    if (userId == null || userId.value().isBlank()) {
      throw new AppException(UserErrorCode.INVALID_USER_ID);
    }
    if (phoneNumber == null || phoneNumber.isBlank()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR);
    }
  }
}
