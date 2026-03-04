package com.uit.se356.core.application.area.dto;

import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.vo.area.BoundingBox;

public record CreateProvinceRequest(
    String code, String name, Double minLat, Double minLng, Double maxLat, Double maxLng) {

  public Province toDomainEntity() {
    BoundingBox boundingBox = new BoundingBox(minLat, minLng, maxLat, maxLng);
    return Province.createNewProvince(code, name, boundingBox);
  }
}
