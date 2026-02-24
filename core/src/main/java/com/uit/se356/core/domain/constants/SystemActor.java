package com.uit.se356.core.domain.constants;

import com.uit.se356.core.domain.vo.authentication.UserId;

public final class SystemActor {

  // Dùng Nil UUID để đại diện cho System Actor
  public static final UserId ID = new UserId("00000000-0000-0000-0000-000000000000");

  private SystemActor() {}
}
