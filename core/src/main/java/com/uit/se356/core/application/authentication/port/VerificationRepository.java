package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.domain.entities.authentication.Verification;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import java.util.Optional;

public interface VerificationRepository {
  Verification save(Verification verification);

  Optional<Verification> findByTokenAndType(String token, VerificationType type);

  void deleteByUserIdAndType(UserId id, VerificationType type);

  void delete(Verification verification);
}
