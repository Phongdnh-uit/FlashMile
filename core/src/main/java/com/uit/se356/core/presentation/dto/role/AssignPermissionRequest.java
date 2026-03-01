package com.uit.se356.core.presentation.dto.role;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignPermissionRequest {
  Set<String> permissionIds;
}
