package com.uit.se356.core.application.contact.handler;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.application.contact.query.GetMyContactsQuery;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.entities.contact.RecipientContact;
import java.util.List;
import java.util.stream.Collectors;

public class GetMyContactsHandler
    implements QueryHandler<GetMyContactsQuery, PageResponse<ContactResult>> {

  private final RecipientContactRepository repository;

  public GetMyContactsHandler(RecipientContactRepository repository) {
    this.repository = repository;
  }

  @Override
  public PageResponse<ContactResult> handle(GetMyContactsQuery query) {
    PageResponse<RecipientContact> domainPage =
        repository.findAll(query.userId(), query.pageable());

    List<ContactResult> resultList =
        domainPage.content().stream().map(ContactResult::fromEntity).collect(Collectors.toList());

    return new PageResponse<>(
        resultList,
        domainPage.page(),
        domainPage.size(),
        domainPage.totalElements(),
        domainPage.totalPages(),
        domainPage.last(),
        domainPage.first());
  }
}
