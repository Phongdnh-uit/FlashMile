package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.VerificationRepository;
import com.uit.se356.core.domain.entities.authentication.Verification;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.VerificationJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.VerificationPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.VerificationJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class VerificationRepositoryImpl implements VerificationRepository {

  private final VerificationJpaRepository verificationJpaRepository;
  private final VerificationPersistenceMapper verificationPersistenceMapper;

  @Override
  @Transactional
  public Verification create(Verification newVerification) {
    VerificationJpaEntity entityToCreate = verificationPersistenceMapper.toEntity(newVerification);
    VerificationJpaEntity savedEntity = verificationJpaRepository.save(entityToCreate);
    return verificationPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public Verification update(Verification verificationToUpdate) {
    // 1. Lấy entity hiện tại
    VerificationJpaEntity existingEntity =
        verificationJpaRepository
            .findById(verificationToUpdate.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Verification not found with id: " + verificationToUpdate.getId().value()));

    // 2. Cập nhật entity với dữ liệu mới từ domain object
    verificationPersistenceMapper.updateEntityFromDomain(verificationToUpdate, existingEntity);

    // 3. Không cần gọi save vì entity tự flush khi hết transaction
    return verificationPersistenceMapper.toDomain(existingEntity);
  }

  @Override
  public Optional<Verification> findByTokenAndType(String token, VerificationType type) {
    return verificationJpaRepository
        .findOne(
            (root, query, builder) ->
                builder.and(
                    builder.equal(root.get("code"), token), builder.equal(root.get("type"), type)))
        .map(verificationPersistenceMapper::toDomain);
  }

  @Transactional
  @Override
  public void deleteByUserIdAndType(UserId id, VerificationType type) {
    verificationJpaRepository.delete(
        (root, query, builder) ->
            builder.and(
                builder.equal(root.get("userId"), id.value()),
                builder.equal(root.get("type"), type)));
  }

  @Transactional
  @Override
  public void delete(Verification verification) {
    verificationJpaRepository.deleteById(verification.getId().value());
  }
}
