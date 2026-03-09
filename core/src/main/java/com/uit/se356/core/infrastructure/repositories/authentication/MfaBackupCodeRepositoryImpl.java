package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.out.MfaBackupCodeRepository;
import com.uit.se356.core.domain.entities.authentication.MfaBackupCode;
import com.uit.se356.core.domain.vo.authentication.MfaBackupCodeId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorBackupCodeJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.MfaBackupCodePersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.MfaBackupCodeJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MfaBackupCodeRepositoryImpl implements MfaBackupCodeRepository {

  private final MfaBackupCodeJpaRepository backupCodeJpaRepository;
  private final MfaBackupCodePersistenceMapper backupCodeMapper;

  @Override
  @Transactional
  public MfaBackupCode create(MfaBackupCode backupCode) {
    MultifactorBackupCodeJpaEntity entityToCreate = backupCodeMapper.toEntity(backupCode);
    MultifactorBackupCodeJpaEntity savedEntity = backupCodeJpaRepository.save(entityToCreate);
    return backupCodeMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public MfaBackupCode update(MfaBackupCode backupCode) {
    MultifactorBackupCodeJpaEntity existingEntity =
        backupCodeJpaRepository
            .findById(backupCode.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "MultifactorBackupCode not found with id: " + backupCode.getId().value()));

    backupCodeMapper.updateEntityFromDomain(backupCode, existingEntity);

    return backupCodeMapper.toDomain(existingEntity);
  }

  @Override
  public Optional<MfaBackupCode> findById(MfaBackupCodeId id) {
    return backupCodeJpaRepository.findById(id.value()).map(backupCodeMapper::toDomain);
  }

  @Override
  @Transactional
  public void deleteById(MfaBackupCodeId id) {
    backupCodeJpaRepository.deleteById(id.value());
  }
}
