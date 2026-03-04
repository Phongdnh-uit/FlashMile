package com.uit.se356.core.infrastructure.persistence.entities.area;

import com.uit.se356.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "provinces")
public class ProvinceJpaEntity extends BaseEntity<String> {
  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

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
