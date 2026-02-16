package com.uit.se356.core.infrastructure.repositories;

import com.uit.se356.core.application.authentication.port.LinkedAccountRepository;
import com.uit.se356.core.domain.entities.authentication.LinkedAccount;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.LinkedAccountJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.LinkedAccountPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.LinkedAccountJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LinkedAccountRepositoryImpl implements LinkedAccountRepository {

  private final LinkedAccountJpaRepository linkedAccountJpaRepository;
  private final LinkedAccountPersistenceMapper linkedAccountPersistenceMapper;

  @Override
  public LinkedAccount save(LinkedAccount linkedAccount) {
    // Kiểm tra xem là cập nhật hay tạo mới
    Optional<LinkedAccountJpaEntity> existingEntity =
        linkedAccountJpaRepository.findById(linkedAccount.getId().value());
    LinkedAccountJpaEntity entityToSave =
        existingEntity
            .map(
                existing -> {
                  linkedAccountPersistenceMapper.updateEntityFromDomain(linkedAccount, existing);
                  return existing;
                })
            .orElseGet(() -> linkedAccountPersistenceMapper.toEntity(linkedAccount));
    LinkedAccountJpaEntity savedEntity = linkedAccountJpaRepository.save(entityToSave);
    return linkedAccountPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<LinkedAccount> findByProviderAndProviderId(String provider, String providerId) {
    Optional<LinkedAccountJpaEntity> jpaEntity =
        linkedAccountJpaRepository.findOne(
            (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("provider"), provider),
                    criteriaBuilder.equal(root.get("providerId"), providerId)));
    return jpaEntity.map(linkedAccountPersistenceMapper::toDomain);
  }

  @Override
  public void delete(LinkedAccount linkedAccount) {
    linkedAccountJpaRepository.deleteById(linkedAccount.getId().value());
  }
}
