package com.uit.se356.core.application.contact.port;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.vo.area.ContactId;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Optional;

public interface RecipientContactRepository {
  RecipientContact create(RecipientContact contact);

  RecipientContact update(RecipientContact contact);

  PageResponse<RecipientContact> findAll(UserId ownerId, SearchPageable pageable);

  Optional<RecipientContact> findByOwnerIdAndPhone(UserId ownerId, PhoneNumber phoneNumber);

  Optional<RecipientContact> findById(ContactId id);

  void delete(ContactId id);

  boolean existsByOwnerIdAndPhone(UserId ownerId, PhoneNumber phoneNumber);
}
