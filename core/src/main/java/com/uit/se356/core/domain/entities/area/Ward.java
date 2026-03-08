package com.uit.se356.core.domain.entities.area;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AreaErrorCode;
import com.uit.se356.core.domain.vo.area.Polygon;
import com.uit.se356.core.domain.vo.area.ProvinceId;
import com.uit.se356.core.domain.vo.area.WardId;
import com.uit.se356.core.domain.vo.area.WardType;
import java.util.Objects;

public class Ward {
  private final WardId id;
  private String code;
  private String name;
  private ProvinceId provinceId;
  private WardType type;
  private Polygon polygon;

  private Ward(
      WardId id, String code, String name, ProvinceId provinceId, WardType type, Polygon polygon) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.provinceId = provinceId;
    this.type = type;
    this.polygon = polygon;
  }

  // Factory method
  public static Ward createNewWard(
      WardId id, String code, String name, ProvinceId provinceId, WardType type, Polygon polygon) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(code);
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);
    Objects.requireNonNull(polygon);

    if (provinceId == null || provinceId.value().isBlank()) {
      throw new AppException(AreaErrorCode.MISSING_PROVINCE_ID);
    }

    return new Ward(id, code, name, provinceId, type, polygon);
  }

  public void update(
      String code, String name, ProvinceId provinceId, WardType type, Polygon polygon) {
    Objects.requireNonNull(code);
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);
    Objects.requireNonNull(polygon);

    if (provinceId == null || provinceId.value().isBlank()) {
      throw new AppException(AreaErrorCode.MISSING_PROVINCE_ID);
    }

    this.code = code;
    this.name = name;
    this.provinceId = provinceId;
    this.type = type;
    this.polygon = polygon;
  }

  public static Ward rehydrate(
      WardId id, String code, String name, ProvinceId provinceId, WardType type, Polygon polygon) {
    return new Ward(id, code, name, provinceId, type, polygon);
  }

  // ---------------------------- Getters ----------------------------
  public WardId getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public ProvinceId getProvinceId() {
    return provinceId;
  }

  public WardType getType() {
    return type;
  }

  public Polygon getPolygon() {
    return polygon;
  }
}
