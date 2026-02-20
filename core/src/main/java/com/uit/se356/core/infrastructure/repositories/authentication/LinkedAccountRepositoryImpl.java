package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.LinkedAccountRepository;
import com.uit.se356.core.domain.entities.authentication.LinkedAccount;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.LinkedAccountJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.LinkedAccountPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.LinkedAccountJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class LinkedAccountRepositoryImpl implements LinkedAccountRepository {

  private final LinkedAccountJpaRepository linkedAccountJpaRepository;
  private final LinkedAccountPersistenceMapper linkedAccountPersistenceMapper;

  @Override
  @Transactional
  public LinkedAccount create(LinkedAccount newLinkedAccount) {
    LinkedAccountJpaEntity entityToCreate =
        linkedAccountPersistenceMapper.toEntity(newLinkedAccount);
    LinkedAccountJpaEntity savedEntity = linkedAccountJpaRepository.save(entityToCreate);
    return linkedAccountPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public LinkedAccount update(LinkedAccount linkedAccountToUpdate) {
    // 1. Lấy lên entity
    LinkedAccountJpaEntity existingEntity =
        linkedAccountJpaRepository
            .findById(linkedAccountToUpdate.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "LinkedAccount not found with id: "
                            + linkedAccountToUpdate.getId().value()));
    // Hard-code exception do đã validate ở service layer rồi, nếu
    // không tìm thấy thì có thể throw exception hoặc handle
    // theo cách khác tùy nhu cầu

    // 2. Cập nhật entity với dữ liệu mới từ domain object
    // linkedAccountPersistenceMapper.updateEntityFromDomain(linkedAccountToUpdate, existingEntity);

    // 3. save không cần gọi vì @Transactional sẽ tự động flush khi transaction kết thúc
    return linkedAccountPersistenceMapper.toDomain(existingEntity);
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
  @Transactional
  public void delete(LinkedAccount linkedAccount) {
    linkedAccountJpaRepository.deleteById(linkedAccount.getId().value());
  }
}
