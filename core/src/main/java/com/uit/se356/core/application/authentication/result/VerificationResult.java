package com.uit.se356.core.application.authentication.result;

public record VerificationResult(String token, Long expiresIn) {}
