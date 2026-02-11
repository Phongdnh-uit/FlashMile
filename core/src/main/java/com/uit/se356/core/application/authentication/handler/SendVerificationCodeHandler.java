package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.authentication.port.VerificationSender;
import com.uit.se356.core.application.authentication.query.SendVerificationCodeQuery;
import com.uit.se356.core.application.authentication.strategies.SendVerificationStrategy;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendVerificationCodeHandler implements QueryHandler<SendVerificationCodeQuery, Void> {

  private final List<SendVerificationStrategy> strategies;
  private final List<VerificationSender> senders;

  @Override
  public Void handle(SendVerificationCodeQuery query) {
    SendVerificationStrategy strategy =
        strategies.stream()
            .filter(s -> s.support(query.purpose()))
            .findFirst()
            .orElseThrow(() -> new AppException(AuthErrorCode.UNCATEGORIZED_EXCEPTION));

    strategy.send(query.recipient(), query.purpose(), query.channel(), senders);
    return null;
  }
}
