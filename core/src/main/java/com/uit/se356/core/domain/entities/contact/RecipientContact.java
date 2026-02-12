package com.uit.se356.core.domain.entities.contact;

import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class RecipientContact {
    private final String id;
    private final UserId ownerId;
    private String name;
    private PhoneNumber phoneNumber;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;

    // Update contact information
    public void updateInfo(
            String name,
            PhoneNumber phoneNumber,
            String note
    ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.note = note;
        this.updatedAt = Instant.now();
    }

    // Factory method to create a new contact
    public static RecipientContact createNewContact(
            UserId ownerId,
            String name,
            PhoneNumber phoneNumber,
            String note
    ) {
        Instant now = Instant.now();
        return new RecipientContact(
                null, // ID will be set by the persistence layer
                ownerId,
                name,
                phoneNumber,
                note,
                now,
                now
        );
    }

    public static RecipientContact rehydrate(
            String id,
            UserId ownerId,
            String name,
            PhoneNumber phoneNumber,
            String note,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new RecipientContact(
                id,
                ownerId,
                name,
                phoneNumber,
                note,
                createdAt,
                updatedAt
        );
    }
}
