package com.uit.se356.core.application.authentication.handler.mfa;

import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.authentication.port.out.MfaRepository;
import com.uit.se356.core.application.authentication.projections.MfaMethodProjection;
import com.uit.se356.core.application.authentication.query.mfa.GetActiveMethodsQuery;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.List;

public class GetActiveMethodsHandler
    implements QueryHandler<GetActiveMethodsQuery, List<MfaMethodProjection>> {

  private final MfaRepository mfaRepository;
  private final SecurityUtil<UserId> securityUtil;

  public GetActiveMethodsHandler(MfaRepository mfaRepository, SecurityUtil<UserId> securityUtil) {
    this.mfaRepository = mfaRepository;
    this.securityUtil = securityUtil;
  }

  @Override
  public List<MfaMethodProjection> handle(GetActiveMethodsQuery query) {
    UserId currentUserId = securityUtil.getCurrentUserPrincipal().get().getId();
    return mfaRepository.findActiveMethodsByUserId(currentUserId);
  }
}
