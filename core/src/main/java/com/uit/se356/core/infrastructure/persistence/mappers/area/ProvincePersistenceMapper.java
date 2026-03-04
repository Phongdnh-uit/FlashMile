package com.uit.se356.core.infrastructure.persistence.mappers.area;

import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.vo.area.BoundingBox;
import com.uit.se356.core.infrastructure.persistence.entities.area.ProvinceJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProvincePersistenceMapper {
  public Province toDomain(ProvinceJpaEntity entity) {
    if (entity == null) return null;

    BoundingBox boundingBox =
        new BoundingBox(
            entity.getMinLat(), entity.getMinLng(),
            entity.getMaxLat(), entity.getMaxLng());

    return Province.rehydrate(entity.getId(), entity.getCode(), entity.getName(), boundingBox);
  }

  public ProvinceJpaEntity toEntity(Province domain) {
    if (domain == null) return null;

    ProvinceJpaEntity entity = new ProvinceJpaEntity();
    entity.setId(domain.getId());
    entity.setCode(domain.getCode());
    entity.setName(domain.getName());

    if (domain.getBoundingBox() != null) {
      entity.setMinLat(domain.getBoundingBox().minLat());
      entity.setMinLng(domain.getBoundingBox().minLng());
      entity.setMaxLat(domain.getBoundingBox().maxLat());
      entity.setMaxLng(domain.getBoundingBox().maxLng());
    }
    return entity;
  }

  public void updateEntityFromDomain(Province province, ProvinceJpaEntity existingEntity) {
    existingEntity.setCode(province.getCode());
    existingEntity.setName(province.getName());

    if (province.getBoundingBox() != null) {
      existingEntity.setMinLat(province.getBoundingBox().minLat());
      existingEntity.setMinLng(province.getBoundingBox().minLng());
      existingEntity.setMaxLat(province.getBoundingBox().maxLat());
      existingEntity.setMaxLng(province.getBoundingBox().maxLng());
    }
  }
}
