package com.uit.se356.core.application.contact.port;

import com.uit.se356.core.domain.entities.contact.RecipientContact;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;

import java.util.List;
import java.util.Optional;

public interface RecipientContactRepository {
    RecipientContact save(RecipientContact contact);

    List<RecipientContact> findAllByOwnerId(UserId ownerId);

    Optional<RecipientContact> findByOwnerIdAndPhone(UserId ownerId, PhoneNumber phoneNumber);

    Optional<RecipientContact> findById(String id);

    void delete(String id);

    boolean existsByOwnerIdAndPhone(UserId ownerId, PhoneNumber phoneNumber);
}
