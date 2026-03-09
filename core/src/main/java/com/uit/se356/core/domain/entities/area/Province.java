package com.uit.se356.core.domain.entities.area;

import com.uit.se356.core.domain.vo.area.ProvinceId;
import com.uit.se356.core.domain.vo.area.ProvinceType;
import java.util.Objects;

public class Province {
  private final ProvinceId id;
  private String code;
  private String name;
  private ProvinceType type;

  // Private constructor
  private Province(ProvinceId id, String code, String name, ProvinceType type) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.type = type;
  }

  // ============================ FACTORY METHODS ============================

  /**
   * Factory method để tạo một tỉnh/thành phố mới (không có ID) Dùng khi tạo mới từ request từ người
   * dùng
   */
  public static Province create(ProvinceId id, String code, String name, ProvinceType type) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(code);
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);
    return new Province(id, code, name, type);
  }

  /**
   * Factory method để khôi phục tỉnh/thành phố từ database Dùng khi ánh xạ từ JPA entity sang
   * domain entity
   */
  public static Province rehydrate(ProvinceId id, String code, String name, ProvinceType type) {
    Objects.requireNonNull(id, "Province id must not be null");
    Objects.requireNonNull(code, "Province code must not be null");
    Objects.requireNonNull(name, "Province name must not be null");
    Objects.requireNonNull(type, "Province type must not be null");
    return new Province(id, code, name, type);
  }

  // ============================ BUSINESS METHODS ============================

  /** Cập nhật thông tin tỉnh/thành phố */
  public void update(String code, String name, ProvinceType type) {
    Objects.requireNonNull(code, "Province code must not be null");
    Objects.requireNonNull(name, "Province name must not be null");
    Objects.requireNonNull(type, "Province type must not be null");
    this.code = code;
    this.name = name;
    this.type = type;
  }

  // ============================ GETTERS ============================

  public ProvinceId getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public ProvinceType getType() {
    return type;
  }

  // ============================ EQUALS & HASHCODE ============================

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Province province = (Province) o;
    return Objects.equals(id, province.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Province{"
        + "id='"
        + id
        + '\''
        + ", code='"
        + code
        + '\''
        + ", name='"
        + name
        + '\''
        + ", type='"
        + type
        + '\''
        + '}';
  }
}
