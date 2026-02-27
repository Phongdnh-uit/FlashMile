package com.uit.se356.core.application.authentication.result;

public record LoginResult(
    String accessToken, String refreshToken, long expiresIn, String tokenType) {}
