package com.uit.se356.core.application.authentication.port.out;

import com.uit.se356.core.domain.entities.authentication.MfaBackupCode;
import com.uit.se356.core.domain.vo.authentication.MfaBackupCodeId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.List;

public interface MfaBackupCodeRepository {

  MfaBackupCode update(MfaBackupCode backupCode);

  List<MfaBackupCode> saveAll(List<MfaBackupCode> backupCodes);

  List<MfaBackupCode> findByUserId(UserId userId);

  void deleteById(MfaBackupCodeId id);

  void deleteAlById(List<MfaBackupCodeId> ids);
}
