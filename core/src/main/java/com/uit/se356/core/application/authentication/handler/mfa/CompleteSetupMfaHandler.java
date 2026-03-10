package com.uit.se356.core.application.authentication.handler.mfa;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.common.utils.OtpGenerator;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.authentication.command.mfa.CompleteSetupMfaCommand;
import com.uit.se356.core.application.authentication.port.out.MfaBackupCodeRepository;
import com.uit.se356.core.application.authentication.port.out.MfaProvider;
import com.uit.se356.core.application.authentication.port.out.MfaRepository;
import com.uit.se356.core.application.authentication.port.out.PasswordEncoder;
import com.uit.se356.core.application.authentication.result.mfa.CompleteSetupMfaResult;
import com.uit.se356.core.domain.entities.authentication.Mfa;
import com.uit.se356.core.domain.entities.authentication.MfaBackupCode;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.MfaBackupCodeId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompleteSetupMfaHandler
    implements CommandHandler<CompleteSetupMfaCommand, CompleteSetupMfaResult> {
  private final List<MfaProvider> mfaProviders;
  private final SecurityUtil<UserId> securityUtil;
  private final MfaRepository mfaRepository;
  private final MfaBackupCodeRepository mfaBackupCodeRepository;
  private final IdGenerator idGenerator;
  private final PasswordEncoder passwordEncoder;

  public CompleteSetupMfaHandler(
      List<MfaProvider> mfaProviders,
      SecurityUtil<UserId> securityUtil,
      MfaRepository mfaRepository,
      IdGenerator idGenerator,
      MfaBackupCodeRepository mfaBackupCodeRepository,
      PasswordEncoder passwordEncoder) {
    this.mfaProviders = mfaProviders;
    this.securityUtil = securityUtil;
    this.mfaRepository = mfaRepository;
    this.mfaBackupCodeRepository = mfaBackupCodeRepository;
    this.idGenerator = idGenerator;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public CompleteSetupMfaResult handle(CompleteSetupMfaCommand command) {
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
    List<String> bkCode = new ArrayList<>();

    List<MfaBackupCode> backupCodes = mfaBackupCodeRepository.findByUserId(userId);
    List<MfaBackupCode> backupCodesToSave = new ArrayList<>();
    boolean isAllRevoked = backupCodes.stream().allMatch(v -> v.getUsedAt() != null);
    if (backupCodes.isEmpty() || isAllRevoked) {
      mfaBackupCodeRepository.deleteAlById(backupCodes.stream().map(MfaBackupCode::getId).toList());
      // Sinh khoảng 10 mã
      for (int i = 0; i < 10; i++) {
        String code = OtpGenerator.generateOtp(8);
        bkCode.add(code);
        String hashedCode = passwordEncoder.encode(code);
        MfaBackupCode backupCode =
            MfaBackupCode.create(
                new MfaBackupCodeId(idGenerator.generate().toString()), userId, hashedCode, null);
        backupCodesToSave.add(backupCode);
      }
      mfaBackupCodeRepository.saveAll(backupCodesToSave);
    }

    CompleteSetupMfaResult result = new CompleteSetupMfaResult(bkCode);

    return result;
  }
}
