package com.uit.se356.core.infrastructure.repositories.area;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.common.utils.PageableUtil;
import com.uit.se356.core.application.area.port.WardRepository;
import com.uit.se356.core.domain.entities.area.Ward;
import com.uit.se356.core.infrastructure.persistence.entities.area.WardJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.area.WardPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.area.WardJpaRepository;
import io.github.perplexhub.rsql.RSQLJPASupport;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class WardRepositoryImpl implements WardRepository {

  private final WardJpaRepository wardJpaRepository;
  private final WardPersistenceMapper wardPersistenceMapper;

  @Override
  public Ward create(Ward ward) {
    WardJpaEntity wardJpaEntity = wardPersistenceMapper.toEntity(ward);
    WardJpaEntity savedEntity = wardJpaRepository.save(wardJpaEntity);
    return wardPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  public Ward update(Ward ward) {
    Optional<WardJpaEntity> existingEntityOpt = wardJpaRepository.findById(ward.getId());
    if (existingEntityOpt.isEmpty()) {
      throw new RuntimeException("Ward not found with id: " + ward.getId());
    }

    WardJpaEntity existingEntity = existingEntityOpt.get();
    wardPersistenceMapper.updateEntityFromDomain(ward, existingEntity);
    WardJpaEntity updatedEntity = wardJpaRepository.save(existingEntity);
    return wardPersistenceMapper.toDomain(updatedEntity);
  }

  @Override
  public Optional<Ward> findById(String id) {
    return wardJpaRepository.findById(id).map(wardPersistenceMapper::toDomain);
  }

  @Override
  public PageResponse<Ward> findAll(SearchPageable pageable) {
    Specification<WardJpaEntity> spec = RSQLJPASupport.toSpecification(pageable.filter());
    Pageable pageableRequest = PageableUtil.createPageable(pageable);
    var page = wardJpaRepository.findAll(spec, pageableRequest);
    return PageResponse.from(page, wardPersistenceMapper::toDomain);
  }

  @Override
  public boolean existsByCode(String code) {
    return wardJpaRepository.existsByCode(code);
  }
}
