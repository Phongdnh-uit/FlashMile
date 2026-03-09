package com.uit.se356.core.application.contact.handler;

import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.application.contact.query.GetContactByPhoneQuery;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;

public class GetContactByPhoneHandler
    implements QueryHandler<GetContactByPhoneQuery, ContactResult> {

  private final RecipientContactRepository repository;

  public GetContactByPhoneHandler(RecipientContactRepository repository) {
    this.repository = repository;
  }

  @Override
  public ContactResult handle(GetContactByPhoneQuery query) {
    // BR Auto-fill: Tìm chính xác contact dựa trên sđt và userId
    return repository
        .findByOwnerIdAndPhone(query.userId(), new PhoneNumber(query.phoneNumber()))
        .map(ContactResult::fromEntity)
        .orElse(null);
  }
}
