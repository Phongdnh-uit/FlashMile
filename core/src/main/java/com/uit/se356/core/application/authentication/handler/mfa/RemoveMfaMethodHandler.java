package com.uit.se356.core.application.authentication.handler.mfa;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.authentication.command.mfa.RemoveMfaMethodCommand;
import com.uit.se356.core.application.authentication.port.out.MfaBackupCodeRepository;
import com.uit.se356.core.application.authentication.port.out.MfaRepository;
import com.uit.se356.core.domain.entities.authentication.Mfa;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Optional;

public class RemoveMfaMethodHandler implements CommandHandler<RemoveMfaMethodCommand, Void> {
  private final MfaRepository mfaRepository;
  private final SecurityUtil<UserId> securityUtil;
  private final MfaBackupCodeRepository mfaBackupCodeRepository;

  public RemoveMfaMethodHandler(
      MfaRepository mfaRepository,
      SecurityUtil<UserId> securityUtil,
      MfaBackupCodeRepository mfaBackupCode) {
    this.mfaRepository = mfaRepository;
    this.securityUtil = securityUtil;
    this.mfaBackupCodeRepository = mfaBackupCode;
  }

  @Override
  public Void handle(RemoveMfaMethodCommand command) {
    // Nếu không còn MFA thì xóa hết Backup Code
    UserId userId = securityUtil.getCurrentUserPrincipal().get().getId();
    Optional<Mfa> mfaMethodOpt = mfaRepository.findByUserIdAndMethod(userId, command.method());
    if (mfaMethodOpt.isEmpty() || !mfaMethodOpt.get().isVerified()) {
      throw new AppException(AuthErrorCode.MFA_METHOD_NOT_FOUND);
    }
    mfaRepository.deleteById(mfaMethodOpt.get().getId());
    var mfaMethods = mfaRepository.findActiveMethodsByUserId(userId);
    if (mfaMethods.isEmpty()) {
      mfaBackupCodeRepository.deleteAllByUserId(userId);
    }
    return null;
  }
}
