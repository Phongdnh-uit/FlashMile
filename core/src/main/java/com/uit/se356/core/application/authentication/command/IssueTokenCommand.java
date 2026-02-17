package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.result.TokenPairResult;
import com.uit.se356.core.domain.vo.authentication.UserId;

public record IssueTokenCommand(UserId userId) implements Command<TokenPairResult> {
  public IssueTokenCommand {
    if (userId == null) {
      throw new AppException(CommonErrorCode.INVALID_ID_FORMAT);
    }
  }
}
