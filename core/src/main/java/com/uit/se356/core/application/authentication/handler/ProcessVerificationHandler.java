package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.authentication.query.ProcessVerificationQuery;
import com.uit.se356.core.application.authentication.result.VerificationResult;
import com.uit.se356.core.application.authentication.strategies.verification.process.ProcessVerificationStrategy;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessVerificationHandler
    implements QueryHandler<ProcessVerificationQuery, VerificationResult> {
  private final List<ProcessVerificationStrategy> strategies;

  @Override
  public VerificationResult handle(ProcessVerificationQuery query) {
    return strategies.stream()
        .filter(strategy -> strategy.support(query.purpose()))
        .findFirst()
        .orElseThrow(() -> new AppException(AuthErrorCode.UNCATEGORIZED_EXCEPTION))
        .process(query.purpose(), query.recipient(), query.code());
  }
}
