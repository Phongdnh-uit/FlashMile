package com.uit.se356.core.application.contact.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.contact.command.CreateContactCommand;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.exception.ContactErrorCode;
import com.uit.se356.core.domain.vo.area.ContactId;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;

public class CreateContactHandler implements CommandHandler<CreateContactCommand, ContactResult> {

  private final RecipientContactRepository repository;
  private final IdGenerator idGenerator;

  public CreateContactHandler(RecipientContactRepository repository, IdGenerator idGenerator) {
    this.repository = repository;
    this.idGenerator = idGenerator;
  }

  @Override
  public ContactResult handle(CreateContactCommand command) {
    PhoneNumber phone = new PhoneNumber(command.phoneNumber());

    // BR: Check Duplicate (Only for New Creation)
    if (repository.existsByOwnerIdAndPhone(command.userId(), phone)) {
      throw new AppException(ContactErrorCode.DUPLICATE_PHONE_NUMBER);
    }

    RecipientContact newContact =
        RecipientContact.create(
            new ContactId(idGenerator.generate().toString()),
            command.userId(),
            command.name(),
            phone,
            command.address(),
            command.note());

    RecipientContact savedContact = repository.create(newContact);
    return ContactResult.fromEntity(savedContact);
  }
}
