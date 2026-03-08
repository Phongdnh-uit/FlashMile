package com.uit.se356.core.application.area.handler;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.area.port.WardRepository;
import com.uit.se356.core.application.area.projections.WardSummaryProjection;
import com.uit.se356.core.application.area.query.WardSummaryQuery;

/** Query Handler để xử lý lấy danh sách Ward */
public class WardSummaryQueryHandler
    implements QueryHandler<WardSummaryQuery, PageResponse<WardSummaryProjection>> {

  private final WardRepository wardRepository;

  public WardSummaryQueryHandler(WardRepository wardRepository) {
    this.wardRepository = wardRepository;
  }

  @Override
  public PageResponse<WardSummaryProjection> handle(WardSummaryQuery query) {
    return wardRepository.findAll(query.pageable());
  }
}
