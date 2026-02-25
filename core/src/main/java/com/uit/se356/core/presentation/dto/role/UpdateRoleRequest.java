package com.uit.se356.core.presentation.dto.role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {
  private String name;
  private String description;
  private boolean isDefault;
}
