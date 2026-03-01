package com.uit.se356.core.application.contact.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.contact.command.CreateContactCommand;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.exception.ContactErrorCode;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;

public class CreateContactHandler implements CommandHandler<CreateContactCommand, ContactResult> {
  private final RecipientContactRepository repository;

  public CreateContactHandler(RecipientContactRepository repository) {
    this.repository = repository;
  }

  @Override
  public ContactResult handle(CreateContactCommand command) {
    UserId ownerId = new UserId(command.userId().value());
    PhoneNumber phone = new PhoneNumber(command.phoneNumber());

    // BR: Check Duplicate (Only for New Creation)
    if (repository.existsByOwnerIdAndPhone(ownerId, phone)) {
      throw new AppException(ContactErrorCode.DUPLICATE_PHONE_NUMBER);
    }

    RecipientContact newContact =
        RecipientContact.createNewContact(
            ownerId, command.name(), phone, command.address(), command.note());

    RecipientContact savedContact = repository.save(newContact);
    return ContactResult.fromEntity(savedContact);
  }
}
