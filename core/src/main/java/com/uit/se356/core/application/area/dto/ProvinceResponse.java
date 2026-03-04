package com.uit.se356.core.application.area.dto;

import com.uit.se356.core.domain.entities.area.Province;

public record ProvinceResponse(
    String id,
    String code,
    String name,
    Double minLat,
    Double minLng,
    Double maxLat,
    Double maxLng) {

  public static ProvinceResponse fromDomain(Province province) {
    return new ProvinceResponse(
        province.getId(),
        province.getCode(),
        province.getName(),
        province.getBoundingBox().minLat(),
        province.getBoundingBox().minLng(),
        province.getBoundingBox().maxLat(),
        province.getBoundingBox().maxLng());
  }
}
