package com.uit.se356.core.infrastructure.repositories;

import com.uit.se356.core.application.authentication.port.VerificationRepository;
import com.uit.se356.core.domain.entities.authentication.Verification;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.VerificationJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.VerificationPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.VerificationJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class VerificationRepositoryImpl implements VerificationRepository {

  private final VerificationJpaRepository verificationJpaRepository;
  private final VerificationPersistenceMapper verificationPersistenceMapper;

  @Transactional
  @Override
  public Verification save(Verification verification) {
    VerificationJpaEntity entity = verificationPersistenceMapper.toEntity(verification);
    VerificationJpaEntity savedEntity = verificationJpaRepository.save(entity);
    return verificationPersistenceMapper.toDomain(savedEntity);
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
