package com.uit.se356.core.application.authentication.result;

import java.time.Instant;

public record RegisterResult(
    String id,
    String fullName,
    String email,
    String phoneNumber,
    boolean emailVerified,
    boolean phoneVerified,
    Instant createdAt,
    Instant updatedAt,
    String createdBy,
    String updatedBy) {}
