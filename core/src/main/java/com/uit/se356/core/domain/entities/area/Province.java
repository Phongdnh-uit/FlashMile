package com.uit.se356.core.domain.entities.area;

import com.uit.se356.core.domain.vo.area.BoundingBox;
import java.util.Objects;

public class Province {
  private final String id;
  private String code;
  private String name;
  private BoundingBox boundingBox;

  // Private constructor
  private Province(String id, String code, String name, BoundingBox boundingBox) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.boundingBox = boundingBox;
  }

  // ============================ FACTORY METHODS ============================

  /**
   * Factory method để tạo một tỉnh/thành phố mới (không có ID) Dùng khi tạo mới từ request từ người
   * dùng
   */
  public static Province create(String id, String code, String name, BoundingBox boundingBox) {
    Objects.requireNonNull(id); // Ép buộc phải có ID
    Objects.requireNonNull(code);
    Objects.requireNonNull(name);
    Objects.requireNonNull(boundingBox);
    return new Province(id, code, name, boundingBox);
  }

  public static Province createNewProvince(String code, String name, BoundingBox boundingBox) {
    Objects.requireNonNull(code, "Province code must not be null");
    Objects.requireNonNull(name, "Province name must not be null");
    Objects.requireNonNull(boundingBox, "Bounding box must not be null");
    return new Province(null, code, name, boundingBox);
  }

  /**
   * Factory method để khôi phục tỉnh/thành phố từ database Dùng khi ánh xạ từ JPA entity sang
   * domain entity
   */
  public static Province rehydrate(String id, String code, String name, BoundingBox boundingBox) {
    Objects.requireNonNull(id, "Province id must not be null");
    Objects.requireNonNull(code, "Province code must not be null");
    Objects.requireNonNull(name, "Province name must not be null");
    Objects.requireNonNull(boundingBox, "Bounding box must not be null");
    return new Province(id, code, name, boundingBox);
  }

  // ============================ BUSINESS METHODS ============================

  /** Cập nhật thông tin tỉnh/thành phố */
  public void updateProvince(String code, String name, BoundingBox boundingBox) {
    Objects.requireNonNull(code, "Province code must not be null");
    Objects.requireNonNull(name, "Province name must not be null");
    Objects.requireNonNull(boundingBox, "Bounding box must not be null");
    this.code = code;
    this.name = name;
    this.boundingBox = boundingBox;
  }

  // ============================ GETTERS ============================

  public String getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public BoundingBox getBoundingBox() {
    return boundingBox;
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
        + '}';
  }
}
