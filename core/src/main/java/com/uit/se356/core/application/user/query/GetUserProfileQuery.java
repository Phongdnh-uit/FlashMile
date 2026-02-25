package com.uit.se356.core.application.user.query;

import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.user.result.UserProfileResult;
import com.uit.se356.core.domain.exception.UserErrorCode;

public record GetUserProfileQuery(String userId) implements Query<UserProfileResult> {
  public GetUserProfileQuery {
    if (userId == null || userId.isBlank()) {
      throw new AppException(UserErrorCode.INVALID_USER_ID);
    }
  }
}
