package com.uit.se356.core.application.authentication.port.out;

import com.uit.se356.core.domain.entities.authentication.MultifactorBackupCode;
import com.uit.se356.core.domain.vo.authentication.MultifactorBackupCodeId;
import java.util.Optional;

public interface MultifactorBackupCodeRepository {

  MultifactorBackupCode create(MultifactorBackupCode backupCode);

  MultifactorBackupCode update(MultifactorBackupCode backupCode);

  Optional<MultifactorBackupCode> findById(MultifactorBackupCodeId id);

  void deleteById(MultifactorBackupCodeId id);
}
