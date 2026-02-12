package com.uit.se356.core.application.contact.mapper;

import com.uit.se356.core.application.contact.dto.ContactResponse;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import org.springframework.stereotype.Component;

@Component
public class RecipientContactMapper {
    public ContactResponse toResponse(RecipientContact savedContact) {
        if (savedContact == null) {
            return null;
        }
        return new ContactResponse(
                savedContact.getId(),
                savedContact.getOwnerId(),
                savedContact.getName(),
                savedContact.getPhoneNumber(),
                savedContact.getNote(),
                savedContact.getCreatedAt(),
                savedContact.getUpdatedAt()
        );
    }
}
