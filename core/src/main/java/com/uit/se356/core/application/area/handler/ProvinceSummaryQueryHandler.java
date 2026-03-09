package com.uit.se356.core.application.area.handler;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.application.area.projections.ProvinceSummaryProjection;
import com.uit.se356.core.application.area.query.ProvinceSummaryQuery;

/** Query Handler để xử lý lấy danh sách Province */
public class ProvinceSummaryQueryHandler
    implements QueryHandler<ProvinceSummaryQuery, PageResponse<ProvinceSummaryProjection>> {

  private final ProvinceRepository provinceRepository;

  public ProvinceSummaryQueryHandler(ProvinceRepository provinceRepository) {
    this.provinceRepository = provinceRepository;
  }

  @Override
  public PageResponse<ProvinceSummaryProjection> handle(ProvinceSummaryQuery query) {
    return provinceRepository.findAll(query.pageable());
  }
}
