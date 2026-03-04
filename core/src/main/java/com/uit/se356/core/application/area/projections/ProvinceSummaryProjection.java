package com.uit.se356.core.application.area.projections;

import com.uit.se356.core.domain.entities.area.Province;

/** Projection dùng để trả về thông tin tóm tắt của Province Dùng khi liệt kê danh sách provinces */
public record ProvinceSummaryProjection(
    String id,
    String code,
    String name,
    Double minLat,
    Double minLng,
    Double maxLat,
    Double maxLng) {

  /** Chuyển đổi từ domain entity sang projection */
  public static ProvinceSummaryProjection fromDomain(Province province) {
    return new ProvinceSummaryProjection(
        province.getId(),
        province.getCode(),
        province.getName(),
        province.getBoundingBox().minLat(),
        province.getBoundingBox().minLng(),
        province.getBoundingBox().maxLat(),
        province.getBoundingBox().maxLng());
  }
}
