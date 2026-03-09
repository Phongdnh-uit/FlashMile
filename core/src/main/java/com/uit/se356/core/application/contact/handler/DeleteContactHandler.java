package com.uit.se356.core.application.contact.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.contact.command.DeleteContactCommand;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.exception.ContactErrorCode;
import java.util.Optional;

public class DeleteContactHandler implements CommandHandler<DeleteContactCommand, Void> {

  private final RecipientContactRepository repository;

  public DeleteContactHandler(RecipientContactRepository repository) {
    this.repository = repository;
  }

  @Override
  public Void handle(DeleteContactCommand command) {
    Optional<RecipientContact> contactOpt = repository.findById(command.contactId());

    if (contactOpt.isEmpty()) {
      throw new AppException(ContactErrorCode.CONTACT_NOT_FOUND);
    }

    RecipientContact contact = contactOpt.get();

    // Verify ownership: contact must belong to the current user
    if (!contact.getOwnerId().equals(command.userId())) {
      throw new AppException(ContactErrorCode.CONTACT_NOT_FOUND);
    }

    repository.delete(command.contactId());
    return null;
  }
}
