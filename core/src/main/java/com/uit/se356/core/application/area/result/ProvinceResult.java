package com.uit.se356.core.application.area.result;

import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.vo.area.BoundingBox;

public record ProvinceResult(String id, String code, String name, BoundingBox boundingBox) {
  public static ProvinceResult fromEntity(Province province) {
    return new ProvinceResult(
        province.getId(), province.getCode(), province.getName(), province.getBoundingBox());
  }
}
