package com.uit.se356.common.security;

import java.util.List;

public interface PermissionScanner {
  List<String> scan(String packageName);
}
