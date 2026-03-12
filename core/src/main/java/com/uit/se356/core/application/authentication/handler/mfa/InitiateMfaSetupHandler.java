package com.uit.se356.core.application.authentication.handler.mfa;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.authentication.command.mfa.InitiateMfaSetupCommand;
import com.uit.se356.core.application.authentication.port.out.MfaProvider;
import com.uit.se356.core.application.authentication.port.out.MfaRepository;
import com.uit.se356.core.application.authentication.result.mfa.MfaSetupResult;
import com.uit.se356.core.domain.entities.authentication.Mfa;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.MfaId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.mfa.MfaConfig;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InitiateMfaSetupHandler
    implements CommandHandler<InitiateMfaSetupCommand, Map<String, String>> {

  private final List<MfaProvider> mfaProviders;
  private final SecurityUtil<UserId> securityUtil;
  private final MfaRepository mfaRepository;
  private final IdGenerator idGenerator;

  public InitiateMfaSetupHandler(
      List<MfaProvider> mfaProviders,
      SecurityUtil<UserId> securityUtil,
      MfaRepository mfaRepository,
      IdGenerator idGenerator) {
    this.mfaProviders = mfaProviders;
    this.securityUtil = securityUtil;
    this.mfaRepository = mfaRepository;
    this.idGenerator = idGenerator;
  }

  @Override
  public Map<String, String> handle(InitiateMfaSetupCommand command) {
    UserId userId = securityUtil.getCurrentUserPrincipal().get().getId();

    MfaProvider provider =
        mfaProviders.stream()
            .filter(p -> p.supports(command.method()))
            .findFirst()
            .orElseThrow(() -> new AppException(CommonErrorCode.INTERNAL_ERROR));

    Optional<Mfa> mfaOpt = mfaRepository.findByUserIdAndMethod(userId, command.method());

    // Trường hợp đã tồn tại:
    // Nếu đã kích hoạt thì dừng
    // Nếu chưa kích hoạt thì tái sử dụng và generate lại secret cũ
    if (mfaOpt.isPresent() && mfaOpt.get().isVerified()) {
      throw new AppException(AuthErrorCode.MFA_METHOD_ALREADY_EXISTS);
    }

    MfaSetupResult<? extends MfaConfig> result = provider.initiateMfaSetup(userId);

    if (result.config() != null) {
        if (mfaOpt.isPresent()) {
          Mfa existingMfa = mfaOpt.get();
          existingMfa.updateConfig(result.config());
          mfaRepository.update(existingMfa);
        } else {
          Mfa newMfa =
              Mfa.create(
                  new MfaId(idGenerator.generate().toString()),
                  userId,
                  command.method(),
                  result.config());
          mfaRepository.create(newMfa);
        }
    }


    // Trả về metadata của result để frontend có thể hiển thị hướng dẫn setup cho người dùng
    return result.metadata();
  }
}
