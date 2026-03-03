package com.uit.se356.core.application.upload.result;

public record PresignedUrlResult(String url, String key, Long expiresInSeconds) {}
