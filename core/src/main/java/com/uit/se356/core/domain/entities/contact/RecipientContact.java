package com.uit.se356.core.domain.entities.contact;

import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipientContact {
  private final String id;
  private final UserId ownerId;
  private String name;
  private PhoneNumber phoneNumber;
  private String address;
  private String note;
  private Instant createdAt;
  private Instant updatedAt;

  // Update contact information
  public void updateInfo(String name, PhoneNumber phoneNumber, String address, String note) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.note = note;
    this.updatedAt = Instant.now();
  }

  // Factory method to create a new contact
  public static RecipientContact createNewContact(
      UserId ownerId, String name, PhoneNumber phoneNumber, String address, String note) {
    Instant now = Instant.now();
    return new RecipientContact(
        null, // ID will be set by the persistence layer
        ownerId,
        name,
        phoneNumber,
        address,
        note,
        now,
        now);
  }

  public static RecipientContact rehydrate(
      String id,
      UserId ownerId,
      String name,
      PhoneNumber phoneNumber,
      String address,
      String note,
      Instant createdAt,
      Instant updatedAt) {
    return new RecipientContact(
        id, ownerId, name, phoneNumber, address, note, createdAt, updatedAt);
  }
}
