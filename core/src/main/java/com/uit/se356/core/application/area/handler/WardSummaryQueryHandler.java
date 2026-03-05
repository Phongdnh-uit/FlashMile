package com.uit.se356.core.application.area.handler;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.area.port.WardRepository;
import com.uit.se356.core.application.area.projections.WardSummaryProjection;
import com.uit.se356.core.application.area.query.WardSummaryQuery;
import com.uit.se356.core.domain.entities.area.Ward;

/** Query Handler để xử lý lấy danh sách Ward */
public class WardSummaryQueryHandler
    implements QueryHandler<WardSummaryQuery, PageResponse<WardSummaryProjection>> {

  private final WardRepository wardRepository;

  public WardSummaryQueryHandler(WardRepository wardRepository) {
    this.wardRepository = wardRepository;
  }

  @Override
  public PageResponse<WardSummaryProjection> handle(WardSummaryQuery query) {
    // Lấy danh sách wards từ repository
    PageResponse<Ward> wards = wardRepository.findAll(query.pageable());

    // Convert domain entities sang projection
    return new PageResponse<>(
        wards.content().stream().map(WardSummaryProjection::fromDomain).toList(),
        wards.page(),
        wards.size(),
        wards.totalElements(),
        wards.totalPages(),
        wards.last(),
        wards.first());
  }
}
