package com.uit.se356.core.application.contact.dto;

import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;

import java.time.Instant;

public record ContactResponse(
        String id,
        UserId ownerId,
        String name,
        PhoneNumber phoneNumber,
        String note,
        Instant createdAt,
        Instant updatedAt
) {
}
