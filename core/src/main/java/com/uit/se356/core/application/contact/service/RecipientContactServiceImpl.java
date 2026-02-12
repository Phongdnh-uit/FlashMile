package com.uit.se356.core.application.contact.service;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.contact.dto.ContactResponse;
import com.uit.se356.core.application.contact.dto.CreateContactRequest;
import com.uit.se356.core.application.contact.mapper.RecipientContactMapper;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipientContactServiceImpl implements RecipientContactService {
    private final RecipientContactMapper mapper;
    private final RecipientContactRepository repository;

    @Override
    public ContactResponse createContact(String userIdStr, CreateContactRequest request) {
        UserId userId = new UserId(userIdStr);
        PhoneNumber phoneNumber = new PhoneNumber(request.phoneNumber());

        if (repository.existsByOwnerIdAndPhone(userId, phoneNumber)) {
            throw new AppException(CommonErrorCode.RESOURCE_ALREADY_EXISTS, "Phone number already exists in your contacts");
        }

        RecipientContact contact = RecipientContact.createNewContact(
                userId,
                request.name(),
                phoneNumber,
                request.note()
        );

        RecipientContact savedContact = repository.save(contact);
        return mapper.toResponse(savedContact);
    }

    @Override
    public ContactResponse getContactByPhoneNumber(String userIdStr, String phoneNumberStr) {
        UserId userId = new UserId(userIdStr);
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberStr);
        return repository.findByOwnerIdAndPhone(userId, phoneNumber)
                .map(mapper::toResponse)
                .orElseThrow(() -> new AppException(CommonErrorCode.RESOURCE_NOT_FOUND, "Contact not found with the given phone number"));
    }

    @Override
    public List<ContactResponse> getMyContacts(String userIdStr) {
        UserId userId = new UserId(userIdStr);

        return repository.findAllByOwnerId(userId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
