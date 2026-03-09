package com.uit.se356.core.application.area.projections;

/** Projection dùng để trả về thông tin tóm tắt của Province Dùng khi liệt kê danh sách provinces */
public interface ProvinceSummaryProjection {
  String getId();

  String getCode();

  String getName();

  String getType();
}
