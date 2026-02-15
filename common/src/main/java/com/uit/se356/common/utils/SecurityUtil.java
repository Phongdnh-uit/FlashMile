package com.uit.se356.common.utils;

import com.uit.se356.common.security.UserPrincipal;
import java.util.Optional;

public interface SecurityUtil<ID> {

  Optional<UserPrincipal<ID>> getCurrentUserPrincipal();

  /**
   * Check if the current user is authenticated (not anonymous).
   *
   * @return true if authenticated, false otherwise.
   */
  boolean isAuthenticated();
}
