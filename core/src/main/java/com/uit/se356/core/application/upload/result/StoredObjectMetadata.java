package com.uit.se356.core.application.upload.result;

import java.time.Instant;

public record StoredObjectMetadata(
    String key, long size, String contentType, Instant lastModified, String etag) {}
