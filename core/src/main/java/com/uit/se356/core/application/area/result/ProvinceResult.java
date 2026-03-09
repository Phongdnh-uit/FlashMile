package com.uit.se356.core.application.area.result;

import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.vo.area.ProvinceId;
import com.uit.se356.core.domain.vo.area.ProvinceType;

public record ProvinceResult(ProvinceId id, String code, String name, ProvinceType type) {
  public static ProvinceResult fromEntity(Province province) {
    return new ProvinceResult(
        province.getId(), province.getCode(), province.getName(), province.getType());
  }
}
