package com.uit.se356.core.application.authentication.port.in;

public interface PermissionChecker {
  void checkCurrentUserHasPermission(String permission);
}
