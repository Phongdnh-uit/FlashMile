package com.uit.se356.core.application.area.port;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.core.domain.entities.area.Ward;
import java.util.Optional;

public interface WardRepository {
  Ward create(Ward ward);

  Ward update(Ward ward);

  Optional<Ward> findById(String id);

  PageResponse<Ward> findAll(SearchPageable pageable);

  boolean existsByCode(String code);
}
