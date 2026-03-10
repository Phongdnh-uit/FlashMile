package com.uit.se356.core.application.authentication.port.out;

import com.uit.se356.core.domain.entities.authentication.Mfa;
import com.uit.se356.core.domain.vo.authentication.MfaId;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Optional;

public interface MfaRepository {

  Mfa create(Mfa multifactor);

  Mfa update(Mfa multifactor);

  Optional<Mfa> findById(MfaId id);

  Optional<Mfa> findByUserIdAndMethod(UserId userId, MfaMethod method);
}
