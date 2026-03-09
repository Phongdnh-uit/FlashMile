package com.uit.se356.core.application.authentication.handler.permission;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.authentication.port.out.PermissionRepository;
import com.uit.se356.core.application.authentication.projections.PermissionSummaryProjection;
import com.uit.se356.core.application.authentication.query.permission.PermissionSummaryQuery;

public class PermissionSummaryQueryHandler
    implements QueryHandler<PermissionSummaryQuery, PageResponse<PermissionSummaryProjection>> {

  private final PermissionRepository permissionRepository;

  public PermissionSummaryQueryHandler(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  @Override
  public PageResponse<PermissionSummaryProjection> handle(PermissionSummaryQuery query) {
    return permissionRepository.findAll(query.pageable());
  }
}
