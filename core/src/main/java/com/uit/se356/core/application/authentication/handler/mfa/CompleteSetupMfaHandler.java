package com.uit.se356.core.application.authentication.handler.mfa;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.authentication.command.mfa.CompleteSetupMfaCommand;
import com.uit.se356.core.application.authentication.port.out.MfaProvider;
import com.uit.se356.core.application.authentication.port.out.MfaRepository;
import com.uit.se356.core.domain.entities.authentication.Mfa;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.List;
import java.util.Optional;

public class CompleteSetupMfaHandler implements CommandHandler<CompleteSetupMfaCommand, Void> {
  private final List<MfaProvider> mfaProviders;
  private final SecurityUtil<UserId> securityUtil;
  private final MfaRepository mfaRepository;

  public CompleteSetupMfaHandler(
      List<MfaProvider> mfaProviders,
      SecurityUtil<UserId> securityUtil,
      MfaRepository mfaRepository) {
    this.mfaProviders = mfaProviders;
    this.securityUtil = securityUtil;
    this.mfaRepository = mfaRepository;
  }

  @Override
  public Void handle(CompleteSetupMfaCommand command) {
    UserId userId = securityUtil.getCurrentUserPrincipal().get().getId();

    MfaProvider provider =
        mfaProviders.stream()
            .filter(p -> p.supports(command.method()))
            .findFirst()
            .orElseThrow(() -> new AppException(CommonErrorCode.INTERNAL_ERROR));

    Optional<Mfa> mfaOpt = mfaRepository.findByUserIdAndMethod(userId, command.method());

    mfaOpt.ifPresentOrElse(
        mfa -> {
          if (mfa.isVerified()) {
            throw new AppException(AuthErrorCode.MFA_METHOD_ALREADY_EXISTS);
          }
          // TODO: Chưa tính đến trường hợp dùng WebAuth
          boolean isValid = provider.verify(mfa.getConfig(), command.credential());
          if (!isValid) {
            throw new AppException(AuthErrorCode.INVALID_CREDENTIALS);
          }
          mfa.markAsVerified();
          mfaRepository.update(mfa);
        },
        () -> {
          throw new AppException(AuthErrorCode.MFA_METHOD_NOT_FOUND);
        });

    // TODO: Backup code, dùng chung backup code cho tất cả các phương thức MFA,  không cần phải
    // tạo mới khi thêm phương thức MFA mới
    return null;
  }
}
