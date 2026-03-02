package com.uit.se356.core.application.authentication.query.role;

import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import java.util.ArrayList;
import java.util.List;

public record GetPermissionsByRoleQuery(RoleId roleId, SearchPageable pageable) {
  public GetPermissionsByRoleQuery {
    List<FieldError> errors = new ArrayList<>();
    if (roleId == null) {
      errors.add(new FieldError("roleId", "validation.field.required", new Object[] {"roleId"}));
    }
  }
}
