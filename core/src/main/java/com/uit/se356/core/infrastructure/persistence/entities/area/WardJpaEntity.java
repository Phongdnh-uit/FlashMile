package com.uit.se356.core.infrastructure.persistence.entities.area;

import com.uit.se356.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "wards",
    indexes = {@Index(name = "idx_ward_province_id", columnList = "province_id")})
public class WardJpaEntity extends BaseEntity<String> {
  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  // Lưu Khóa ngoại tham chiếu đến Province
  @Column(name = "province_id", nullable = false)
  private String provinceId;

  // 4 góc của Bounding Box
  @Column(name = "min_lat", nullable = false)
  private Double minLat;

  @Column(name = "min_lng", nullable = false)
  private Double minLng;

  @Column(name = "max_lat", nullable = false)
  private Double maxLat;

  @Column(name = "max_lng", nullable = false)
  private Double maxLng;
}
