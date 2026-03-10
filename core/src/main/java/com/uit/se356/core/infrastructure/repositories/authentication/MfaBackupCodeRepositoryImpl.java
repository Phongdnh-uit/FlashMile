package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.out.MfaBackupCodeRepository;
import com.uit.se356.core.domain.entities.authentication.MfaBackupCode;
import com.uit.se356.core.domain.vo.authentication.MfaBackupCodeId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorBackupCodeJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.MfaBackupCodePersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.MfaBackupCodeJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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

  @Override
  public List<MfaBackupCode> findByUserId(UserId userId) {
    return backupCodeJpaRepository
        .findAll((root, query, builder) -> builder.equal(root.get("userId"), userId.value()))
        .stream()
        .map(backupCodeMapper::toDomain)
        .toList();
  }

  @Override
  public void deleteAlById(List<MfaBackupCodeId> ids) {
    List<String> idValues = ids.stream().map(id -> id.value()).toList();
    backupCodeJpaRepository.deleteAllById(idValues);
  }

  @Override
  public List<MfaBackupCode> saveAll(List<MfaBackupCode> backupCodes) {
    List<MultifactorBackupCodeJpaEntity> entities =
        backupCodes.stream().map(backupCodeMapper::toEntity).toList();

    List<MultifactorBackupCodeJpaEntity> savedEntities = backupCodeJpaRepository.saveAll(entities);

    return savedEntities.stream().map(backupCodeMapper::toDomain).toList();
  }
}
