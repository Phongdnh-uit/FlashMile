package com.uit.se356.core.application.contact.service;

import com.uit.se356.core.application.contact.dto.ContactResponse;
import com.uit.se356.core.application.contact.dto.CreateContactRequest;

import java.util.List;

public interface RecipientContactService {
    ContactResponse createContact(String userIdStr, CreateContactRequest request);
    ContactResponse getContactByPhoneNumber(String userIdStr, String phoneNumberStr);
    List<ContactResponse> getMyContacts(String userIdStr);
}
