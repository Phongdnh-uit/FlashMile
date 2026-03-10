package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.out.MfaRepository;
import com.uit.se356.core.application.authentication.projections.MfaMethodProjection;
import com.uit.se356.core.domain.entities.authentication.Mfa;
import com.uit.se356.core.domain.vo.authentication.MfaId;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.MfaPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.MfaJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MfaRepositoryImpl implements MfaRepository {

  private final MfaJpaRepository multifactorJpaRepository;
  private final MfaPersistenceMapper multifactorMapper;

  @Override
  @Transactional
  public Mfa create(Mfa multifactor) {
    MultifactorJpaEntity entityToCreate = multifactorMapper.toEntity(multifactor);
    MultifactorJpaEntity savedEntity = multifactorJpaRepository.save(entityToCreate);
    return multifactorMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public Mfa update(Mfa multifactor) {
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
  public Optional<Mfa> findById(MfaId id) {
    return multifactorJpaRepository.findById(id.value()).map(multifactorMapper::toDomain);
  }

  @Override
  public Optional<Mfa> findByUserIdAndMethod(UserId userId, MfaMethod method) {
    return multifactorJpaRepository
        .findOne(
            (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("userId"), userId.value()),
                    criteriaBuilder.equal(root.get("method"), method.name())))
        .map(multifactorMapper::toDomain);
  }

  @Override
  public List<MfaMethodProjection> findActiveMethodsByUserId(UserId userId) {
    return multifactorJpaRepository.findBy(
        (root, query, criteriaBuilder) ->
            criteriaBuilder.and(
                criteriaBuilder.equal(root.get("userId"), userId.value()),
                criteriaBuilder.isTrue(root.get("isVerified"))),
        q -> q.as(MfaMethodProjection.class).all());
  }

  @Override
  public void deleteById(MfaId id) {
    multifactorJpaRepository.deleteById(id.value());
  }
}
