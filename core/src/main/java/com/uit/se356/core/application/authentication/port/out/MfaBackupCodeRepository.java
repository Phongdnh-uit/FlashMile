package com.uit.se356.core.application.authentication.port.out;

import com.uit.se356.core.domain.entities.authentication.MfaBackupCode;
import com.uit.se356.core.domain.vo.authentication.MfaBackupCodeId;
import java.util.Optional;

public interface MfaBackupCodeRepository {

  MfaBackupCode create(MfaBackupCode backupCode);

  MfaBackupCode update(MfaBackupCode backupCode);

  Optional<MfaBackupCode> findById(MfaBackupCodeId id);

  void deleteById(MfaBackupCodeId id);
}
