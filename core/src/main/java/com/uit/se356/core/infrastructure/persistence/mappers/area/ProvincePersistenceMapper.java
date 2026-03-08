package com.uit.se356.core.infrastructure.persistence.mappers.area;

import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.vo.area.ProvinceId;
import com.uit.se356.core.infrastructure.persistence.entities.area.ProvinceJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProvincePersistenceMapper {
  public Province toDomain(ProvinceJpaEntity entity) {
    if (entity == null) return null;

    return Province.rehydrate(
        new ProvinceId(entity.getId()), entity.getCode(), entity.getName(), entity.getType());
  }

  public ProvinceJpaEntity toEntity(Province domain) {
    if (domain == null) return null;

    ProvinceJpaEntity entity = new ProvinceJpaEntity();
    entity.setId(domain.getId().value());
    entity.setCode(domain.getCode());
    entity.setName(domain.getName());
    entity.setType(domain.getType());
    return entity;
  }

  public void updateEntityFromDomain(Province province, ProvinceJpaEntity existingEntity) {
    existingEntity.setCode(province.getCode());
    existingEntity.setName(province.getName());
    existingEntity.setType(province.getType());
  }
}
