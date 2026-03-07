package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.out.MultifactorRepository;
import com.uit.se356.core.domain.entities.authentication.Multifactor;
import com.uit.se356.core.domain.vo.authentication.MultifactorId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.MultifactorPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.MultifactorJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MultifactorRepositoryImpl implements MultifactorRepository {

  private final MultifactorJpaRepository multifactorJpaRepository;
  private final MultifactorPersistenceMapper multifactorMapper;

  @Override
  @Transactional
  public Multifactor create(Multifactor multifactor) {
    MultifactorJpaEntity entityToCreate = multifactorMapper.toEntity(multifactor);
    MultifactorJpaEntity savedEntity = multifactorJpaRepository.save(entityToCreate);
    return multifactorMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public Multifactor update(Multifactor multifactor) {
    MultifactorJpaEntity existingEntity =
        multifactorJpaRepository
            .findById(multifactor.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Multifactor not found with id: " + multifactor.getId().value()));

    multifactorMapper.updateEntityFromDomain(multifactor, existingEntity);

    return multifactorMapper.toDomain(existingEntity);
  }

  @Override
  public Optional<Multifactor> findById(MultifactorId id) {
    return multifactorJpaRepository.findById(id.value()).map(multifactorMapper::toDomain);
  }
}
