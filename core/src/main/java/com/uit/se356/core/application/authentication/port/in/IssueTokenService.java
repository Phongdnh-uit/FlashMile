package com.uit.se356.core.application.authentication.port.in;

import com.uit.se356.core.application.authentication.command.IssueTokenCommand;
import com.uit.se356.core.application.authentication.result.TokenPairResult;

public interface IssueTokenService {
  TokenPairResult issueToken(IssueTokenCommand command);

  String hashToken(String token);
}
