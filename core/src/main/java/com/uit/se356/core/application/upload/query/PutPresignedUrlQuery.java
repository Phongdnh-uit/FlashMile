package com.uit.se356.core.application.upload.query;

public record PutPresignedUrlQuery(
    String storageKey, String contentType, long contentLength, Long expirationSeconds) {}
