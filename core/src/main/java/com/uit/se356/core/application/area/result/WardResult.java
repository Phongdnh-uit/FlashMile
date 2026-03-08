package com.uit.se356.core.application.area.result;

import com.uit.se356.core.domain.entities.area.Ward;
import com.uit.se356.core.domain.vo.area.Polygon;
import com.uit.se356.core.domain.vo.area.ProvinceId;
import com.uit.se356.core.domain.vo.area.WardId;
import com.uit.se356.core.domain.vo.area.WardType;

public record WardResult(
    WardId id, String code, String name, ProvinceId provinceId, WardType type, Polygon polygon) {
  public static WardResult fromEntity(Ward ward) {
    return new WardResult(
        ward.getId(),
        ward.getCode(),
        ward.getName(),
        ward.getProvinceId(),
        ward.getType(),
        ward.getPolygon());
  }
}
