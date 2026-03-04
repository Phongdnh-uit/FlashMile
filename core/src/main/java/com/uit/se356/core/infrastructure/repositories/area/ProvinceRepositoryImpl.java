package com.uit.se356.core.infrastructure.repositories.area;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.utils.PageableUtil;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.exception.AreaErrorCode;
import com.uit.se356.core.infrastructure.persistence.entities.area.ProvinceJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.area.ProvincePersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.area.ProvinceJpaRepository;
import io.github.perplexhub.rsql.RSQLJPASupport;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProvinceRepositoryImpl implements ProvinceRepository {

  private final ProvinceJpaRepository provinceJpaRepository;
  private final ProvincePersistenceMapper provincePersistenceMapper;

  @Override
  public Province create(Province province) {
    ProvinceJpaEntity entity = provincePersistenceMapper.toEntity(province);
    ProvinceJpaEntity savedProvince = provinceJpaRepository.save(entity);
    return provincePersistenceMapper.toDomain(savedProvince);
  }

  @Override
  public Province update(Province province) {
    ProvinceJpaEntity existingEntity =
        provinceJpaRepository
            .findById(province.getId())
            .orElseThrow(
                () -> new AppException(AreaErrorCode.PROVINCE_NOT_FOUND, province.getId()));
    provincePersistenceMapper.updateEntityFromDomain(province, existingEntity);
    ProvinceJpaEntity updatedEntity = provinceJpaRepository.save(existingEntity);
    return provincePersistenceMapper.toDomain(updatedEntity);
  }

  @Override
  public Optional<Province> findById(String id) {
    return provinceJpaRepository.findById(id).map(provincePersistenceMapper::toDomain);
  }

  @Override
  public PageResponse<Province> findAll(SearchPageable pageable) {
    Specification<ProvinceJpaEntity> spec = RSQLJPASupport.toSpecification(pageable.filter());
    Pageable pageableRequest = PageableUtil.createPageable(pageable);
    var page = provinceJpaRepository.findAll(spec, pageableRequest);
    return PageResponse.from(page, provincePersistenceMapper::toDomain);
  }

  @Override
  public boolean existsByCode(String code) {
    return provinceJpaRepository.existsByCode(code);
  }
}
