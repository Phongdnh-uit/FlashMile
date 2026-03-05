package com.uit.se356.core.infrastructure.persistence.mappers.area;

import com.uit.se356.core.domain.entities.area.Ward;
import com.uit.se356.core.domain.vo.area.BoundingBox;
import com.uit.se356.core.infrastructure.persistence.entities.area.WardJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class WardPersistenceMapper {
  public Ward toDomain(WardJpaEntity entity) {
    if (entity == null) return null;

    BoundingBox boundingBox =
        new BoundingBox(
            entity.getMinLat(), entity.getMinLng(),
            entity.getMaxLat(), entity.getMaxLng());

    return Ward.rehydrate(
        entity.getId(), entity.getCode(), entity.getName(), entity.getProvinceId(), boundingBox);
  }

  public WardJpaEntity toEntity(Ward domain) {
    if (domain == null) return null;

    WardJpaEntity entity = new WardJpaEntity();
    entity.setId(domain.getId());
    entity.setCode(domain.getCode());
    entity.setName(domain.getName());
    entity.setProvinceId(domain.getProvinceId());

    if (domain.getBoundingBox() != null) {
      entity.setMinLat(domain.getBoundingBox().minLat());
      entity.setMinLng(domain.getBoundingBox().minLng());
      entity.setMaxLat(domain.getBoundingBox().maxLat());
      entity.setMaxLng(domain.getBoundingBox().maxLng());
    }
    return entity;
  }

  public void updateEntityFromDomain(Ward domain, WardJpaEntity entity) {
    if (domain == null || entity == null) return;

    entity.setCode(domain.getCode());
    entity.setName(domain.getName());
    entity.setProvinceId(domain.getProvinceId());

    if (domain.getBoundingBox() != null) {
      entity.setMinLat(domain.getBoundingBox().minLat());
      entity.setMinLng(domain.getBoundingBox().minLng());
      entity.setMaxLat(domain.getBoundingBox().maxLat());
      entity.setMaxLng(domain.getBoundingBox().maxLng());
    }
  }
}
