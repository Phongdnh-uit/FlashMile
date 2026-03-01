package com.uit.se356.core.presentation.rest.contact;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.security.UserPrincipal;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.contact.command.CreateContactCommand;
import com.uit.se356.core.application.contact.query.GetContactByPhoneQuery;
import com.uit.se356.core.application.contact.query.GetMyContactsQuery;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Recipient Contacts")
@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class RecipientContactController {

  private final QueryBus queryBus;
  private final CommandBus commandBus;
  private final SecurityUtil<UserId> securityUtil;

  @Operation(summary = "Get my address book")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ContactResult>>> getMyContacts() {
    UserId currentUserId = getCurrentUserId();

    GetMyContactsQuery query = new GetMyContactsQuery(currentUserId);
    List<ContactResult> results = queryBus.dispatch(query);

    return ResponseEntity.ok(ApiResponse.ok(results, "Contacts retrieved successfully"));
  }

  @Operation(summary = "Auto-fill lookup by phone number")
  @GetMapping("/lookup")
  public ResponseEntity<ApiResponse<ContactResult>> lookupContact(@RequestParam String phone) {
    UserId currentUserId = getCurrentUserId();

    GetContactByPhoneQuery query = new GetContactByPhoneQuery(currentUserId, phone);
    ContactResult result = queryBus.dispatch(query);

    if (result == null) {
      return ResponseEntity.ok(ApiResponse.ok(null, "No contact found for auto-fill"));
    }
    return ResponseEntity.ok(ApiResponse.ok(result, "Contact found"));
  }

  @Operation(summary = "Add a new contact to address book")
  @PostMapping
  public ResponseEntity<ApiResponse<ContactResult>> createContact(
      @RequestBody CreateContactCommand request) {

    UserId currentUserId = getCurrentUserId();

    CreateContactCommand command =
        new CreateContactCommand(
            currentUserId,
            request.name(),
            request.phoneNumber(),
            request.address(),
            request.note());

    ContactResult result = commandBus.dispatch(command);

    return ResponseEntity.ok(ApiResponse.created(result, "Contact saved successfully"));
  }

  private UserId getCurrentUserId() {
    return securityUtil
        .getCurrentUserPrincipal()
        .map(UserPrincipal::getId)
        .orElseThrow(() -> new AppException(AuthErrorCode.INVALID_CREDENTIALS));
  }
}
