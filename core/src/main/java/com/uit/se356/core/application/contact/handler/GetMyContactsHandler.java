package com.uit.se356.core.application.contact.handler;

import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.application.contact.query.GetMyContactsQuery;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetMyContactsHandler implements QueryHandler<GetMyContactsQuery, List<ContactResult>> {

  private final RecipientContactRepository repository;

  public GetMyContactsHandler(RecipientContactRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<ContactResult> handle(GetMyContactsQuery query) {
    // Lấy contacts của user và sắp xếp theo tên (BR: Sort alphabetically by Name ASC)
    return repository.findAllByOwnerId(new UserId(query.userId().value())).stream()
        .sorted(
            Comparator.comparing(
                c -> c.getName().toLowerCase())) // Sắp xếp không phân biệt chữ hoa thường
        .map(ContactResult::fromEntity)
        .collect(Collectors.toList());
  }
}
