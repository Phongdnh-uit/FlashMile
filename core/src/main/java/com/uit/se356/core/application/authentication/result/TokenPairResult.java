package com.uit.se356.core.application.authentication.result;

public record TokenPairResult(
    String accessToken, String refreshToken, Long expiresIn, String tokenType) {}
