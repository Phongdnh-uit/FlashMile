package com.uit.se356.core.application.internal.handler;

import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.authentication.port.CacheRepository;
import com.uit.se356.core.application.internal.query.DebugOtpQuery;
import com.uit.se356.core.application.internal.result.DebugOtpResult;
import com.uit.se356.core.domain.constants.CacheKey;

public class DebugOtpHandler implements QueryHandler<DebugOtpQuery, DebugOtpResult> {

  private final CacheRepository cacheRepository;

  public DebugOtpHandler(CacheRepository cacheRepository) {
    this.cacheRepository = cacheRepository;
  }

  @Override
  public DebugOtpResult handle(DebugOtpQuery query) {
    String cacheKey = CacheKey.PHONE_VERIFICATION_CODE_PREFIX + ":" + query.phoneNumber();
    return cacheRepository
        .get(cacheKey)
        .map(otp -> new DebugOtpResult(otp))
        .orElseGet(() -> new DebugOtpResult(null));
  }
}
