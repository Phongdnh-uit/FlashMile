package com.uit.se356.core.application.authentication.result;

public record RegisterResult(
    String id,
    String fullName,
    String email,
    String phoneNumber,
    boolean emailVerified,
    boolean phoneVerified) {}
