package com.uit.se356.core.infrastructure.persistence.mappers.area;

import com.uit.se356.core.domain.entities.area.Ward;
import com.uit.se356.core.domain.vo.area.Polygon;
import com.uit.se356.core.domain.vo.area.ProvinceId;
import com.uit.se356.core.domain.vo.area.WardId;
import com.uit.se356.core.infrastructure.persistence.entities.area.WardJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class WardPersistenceMapper {
  private final ObjectMapper objectMapper;

  public Ward toDomain(WardJpaEntity entity) {
    if (entity == null) return null;

    Polygon polygon = null;
    try {
      if (entity.getPolygon() != null) {
        polygon = objectMapper.readValue(entity.getPolygon(), Polygon.class);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize Polygon from JSON", e);
    }

    return Ward.rehydrate(
        new WardId(entity.getId()),
        entity.getCode(),
        entity.getName(),
        new ProvinceId(entity.getProvinceId()),
        entity.getType(),
        polygon);
  }

  public WardJpaEntity toEntity(Ward domain) {
    if (domain == null) return null;

    WardJpaEntity entity = new WardJpaEntity();
    entity.setId(domain.getId().value());
    entity.setCode(domain.getCode());
    entity.setName(domain.getName());
    entity.setProvinceId(domain.getProvinceId().value());
    entity.setType(domain.getType());

    try {
      String polygonJson = objectMapper.writeValueAsString(domain.getPolygon());
      entity.setPolygon(polygonJson);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize Polygon to JSON", e);
    }
    return entity;
  }

  public void updateEntityFromDomain(Ward domain, WardJpaEntity entity) {
    if (domain == null || entity == null) return;

    entity.setCode(domain.getCode());
    entity.setName(domain.getName());
    entity.setProvinceId(domain.getProvinceId().value());
    entity.setType(domain.getType());

    if (domain.getPolygon() != null) {
      try {
        entity.setPolygon(objectMapper.writeValueAsString(domain.getPolygon()));
      } catch (Exception e) {
        // Log error and skip
      }
    }
  }
}
