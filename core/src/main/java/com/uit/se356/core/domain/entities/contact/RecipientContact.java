package com.uit.se356.core.domain.entities.contact;

import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.time.Instant;

public class RecipientContact {
  private final String id;
  private final UserId ownerId;
  private String name;
  private PhoneNumber phoneNumber;
  private String address;
  private String note;

  // Private constructor for rehydration
  private RecipientContact(
      Object o, UserId ownerId, String name, PhoneNumber phoneNumber, String address, String note) {
    this.id = (String) o;
    this.ownerId = ownerId;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.note = note;
  }

  // Update contact information
  public void updateInfo(String name, PhoneNumber phoneNumber, String address, String note) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.note = note;
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
        note);
  }

  public static RecipientContact rehydrate(
      String id,
      UserId ownerId,
      String name,
      PhoneNumber phoneNumber,
      String address,
      String note) {
    return new RecipientContact(id, ownerId, name, phoneNumber, address, note);
  }

  // ============================ GETTERS ============================
  public String getId() {
    return id;
  }

  public UserId getOwnerId() {
    return ownerId;
  }

  public String getName() {
    return name;
  }

  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public String getNote() {
    return note;
  }
}
