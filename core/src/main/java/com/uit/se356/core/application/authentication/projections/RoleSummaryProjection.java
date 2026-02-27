package com.uit.se356.core.application.authentication.projections;

public interface RoleSummaryProjection {
  String getId();

  String getName();

  boolean getisDefault();

  boolean getSystemRole();
}
