package com.uit.se356.core.application.authentication.handler.permission;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.authentication.port.out.PermissionRepository;
import com.uit.se356.core.application.authentication.projections.PermissionSummaryProjection;
import com.uit.se356.core.application.authentication.query.role.GetPermissionsByRoleQuery;

public class GetPermissionByRoleHandler
    implements QueryHandler<GetPermissionsByRoleQuery, PageResponse<PermissionSummaryProjection>> {

  private final PermissionRepository permissionRepository;

  public GetPermissionByRoleHandler(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  @Override
  public PageResponse<PermissionSummaryProjection> handle(GetPermissionsByRoleQuery query) {
    return permissionRepository.findAllByRoleId(query.roleId(), query.pageable());
  }
}
