package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.out.MultifactorBackupCodeRepository;
import com.uit.se356.core.domain.entities.authentication.MultifactorBackupCode;
import com.uit.se356.core.domain.vo.authentication.MultifactorBackupCodeId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorBackupCodeJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.MultifactorBackupCodePersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.MultifactorBackupCodeJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MultifactorBackupCodeRepositoryImpl implements MultifactorBackupCodeRepository {

  private final MultifactorBackupCodeJpaRepository backupCodeJpaRepository;
  private final MultifactorBackupCodePersistenceMapper backupCodeMapper;

  @Override
  @Transactional
  public MultifactorBackupCode create(MultifactorBackupCode backupCode) {
    MultifactorBackupCodeJpaEntity entityToCreate = backupCodeMapper.toEntity(backupCode);
    MultifactorBackupCodeJpaEntity savedEntity = backupCodeJpaRepository.save(entityToCreate);
    return backupCodeMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public MultifactorBackupCode update(MultifactorBackupCode backupCode) {
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
  public Optional<MultifactorBackupCode> findById(MultifactorBackupCodeId id) {
    return backupCodeJpaRepository.findById(id.value()).map(backupCodeMapper::toDomain);
  }

  @Override
  @Transactional
  public void deleteById(MultifactorBackupCodeId id) {
    backupCodeJpaRepository.deleteById(id.value());
  }
}
