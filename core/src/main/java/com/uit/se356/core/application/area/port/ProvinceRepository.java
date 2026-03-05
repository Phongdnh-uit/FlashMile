package com.uit.se356.core.application.area.port;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.core.domain.entities.area.Province;
import java.util.Optional;

public interface ProvinceRepository {
  Province create(Province province);

  Province update(Province province);

  Optional<Province> findById(String id);

  PageResponse<Province> findAll(SearchPageable pageable);

  boolean existsByCode(String code);

  void deleteById(String id);

  Optional<Province> findByCode(String provinceCode);
}
