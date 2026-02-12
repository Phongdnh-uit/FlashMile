package com.uit.se356.core.presentation.rest.contact;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.core.application.contact.dto.ContactResponse;
import com.uit.se356.core.application.contact.dto.CreateContactRequest;
import com.uit.se356.core.application.contact.service.RecipientContactService;
import com.uit.se356.core.application.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class RecipientContactController {

    private final RecipientContactService contactService;

    @PostMapping
    public ApiResponse<ContactResponse> createContact(@RequestBody @Valid CreateContactRequest request) {
        // TODO: Lấy userId từ Security Context
        String currentUserId = "user-id-from-token";
        return ApiResponse.created(contactService.createContact(currentUserId, request), "Contact created successfully");
    }

    @GetMapping
    public ApiResponse<List<ContactResponse>> getMyContacts() {
        String currentUserId = "user-id-from-token";
        return ApiResponse.ok(contactService.getMyContacts(currentUserId), "Fetch contacts success");
    }

    @GetMapping("/lookup")
    public ApiResponse<ContactResponse> lookupContact(@RequestParam String phone) {
        String currentUserId = "user-id-from-token";
        ContactResponse contact = contactService.getContactByPhoneNumber(currentUserId, phone);
        if (contact == null) {
            return ApiResponse.noContent("No contact found");
        }
        return ApiResponse.ok(contact, "Contact found");
    }

}
