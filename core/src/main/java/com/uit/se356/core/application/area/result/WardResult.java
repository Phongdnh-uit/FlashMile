package com.uit.se356.core.application.area.result;

import com.uit.se356.core.domain.entities.area.Ward;
import com.uit.se356.core.domain.vo.area.BoundingBox;

public record WardResult(
    String id, String code, String name, String provinceId, BoundingBox boundingBox) {
  public static WardResult fromEntity(Ward ward) {
    return new WardResult(
        ward.getId(), ward.getCode(), ward.getName(), ward.getProvinceId(), ward.getBoundingBox());
  }
}
