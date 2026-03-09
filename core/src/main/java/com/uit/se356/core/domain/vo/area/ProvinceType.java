package com.uit.se356.core.domain.vo.area;

public enum ProvinceType {
  CITY("Thành phố"),
  PROVINCE("Tỉnh");

  private final String displayName;

  ProvinceType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
