package com.uit.se356.core.application.contact.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.contact.command.UpdateContactCommand;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.exception.ContactErrorCode;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import java.util.Optional;

public class UpdateContactHandler implements CommandHandler<UpdateContactCommand, ContactResult> {

  private final RecipientContactRepository repository;

  public UpdateContactHandler(RecipientContactRepository repository) {
    this.repository = repository;
  }

  @Override
  public ContactResult handle(UpdateContactCommand command) {
    Optional<RecipientContact> contactOpt = repository.findById(command.contactId());

    if (contactOpt.isEmpty()) {
      throw new AppException(ContactErrorCode.CONTACT_NOT_FOUND);
    }

    RecipientContact existingContact = contactOpt.get();

    // Verify ownership: contact must belong to the current user
    if (!existingContact.getOwnerId().equals(command.userId())) {
      throw new AppException(ContactErrorCode.CONTACT_NOT_FOUND);
    }

    PhoneNumber newPhone = new PhoneNumber(command.phoneNumber());

    // Check if new phone number already exists (only if phone changed)
    if (!existingContact.getPhoneNumber().equals(newPhone)) {
      if (repository.existsByOwnerIdAndPhone(command.userId(), newPhone)) {
        throw new AppException(ContactErrorCode.DUPLICATE_PHONE_NUMBER);
      }
    }

    // Update contact information
    existingContact.updateInfo(command.name(), newPhone, command.address(), command.note());

    RecipientContact updatedContact = repository.update(existingContact);
    return ContactResult.fromEntity(updatedContact);
  }
}
