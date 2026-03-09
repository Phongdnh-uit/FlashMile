package com.uit.se356.core.application.contact.result;

import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.vo.area.ContactId;

public record ContactResult(
    ContactId id, String name, String phoneNumber, String address, String note) {
  public static ContactResult fromEntity(RecipientContact contact) {
    return new ContactResult(
        contact.getId(),
        contact.getName(),
        contact.getPhoneNumber().value(),
        contact.getAddress(),
        contact.getNote());
  }
}
