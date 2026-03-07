package com.uit.se356.core.application.authentication.port.out;

import com.uit.se356.core.domain.entities.authentication.Multifactor;
import com.uit.se356.core.domain.vo.authentication.MultifactorId;
import java.util.Optional;

public interface MultifactorRepository {

  Multifactor create(Multifactor multifactor);

  Multifactor update(Multifactor multifactor);

  Optional<Multifactor> findById(MultifactorId id);
}
