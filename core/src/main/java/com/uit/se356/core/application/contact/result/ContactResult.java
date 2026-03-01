package com.uit.se356.core.application.contact.result;

import com.uit.se356.core.domain.entities.contact.RecipientContact;

public record ContactResult(
    String id,
    String name,
    String phoneNumber,
    String address,
    String note,
    String createdAt,
    String updatedAt) {
  public static ContactResult fromEntity(RecipientContact contact) {
    return new ContactResult(
        contact.getId(),
        contact.getName(),
        contact.getPhoneNumber().value(),
        contact.getAddress(),
        contact.getNote(),
        contact.getCreatedAt().toString(),
        contact.getUpdatedAt().toString());
  }
}
