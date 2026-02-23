package com.uit.se356.common.security;

import com.uit.se356.common.dto.PermissionDefinition;
import java.util.List;

public interface PermissionScanner {
  List<PermissionDefinition> scan(String packageName);
}
