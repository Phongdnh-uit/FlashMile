package com.uit.se356.core.application.area.projections;

import com.uit.se356.core.domain.entities.area.Ward;

/** Projection dùng để trả về thông tin tóm tắt của Ward Dùng khi liệt kê danh sách wards */
public record WardSummaryProjection(
    String id,
    String code,
    String name,
    String provinceId,
    Double minLat,
    Double minLng,
    Double maxLat,
    Double maxLng) {

  /** Chuyển đổi từ domain entity sang projection */
  public static WardSummaryProjection fromDomain(Ward ward) {
    return new WardSummaryProjection(
        ward.getId(),
        ward.getCode(),
        ward.getName(),
        ward.getProvinceId(),
        ward.getBoundingBox().minLat(),
        ward.getBoundingBox().minLng(),
        ward.getBoundingBox().maxLat(),
        ward.getBoundingBox().maxLng());
  }
}
