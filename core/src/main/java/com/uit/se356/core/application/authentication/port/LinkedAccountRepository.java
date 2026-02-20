package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.domain.entities.authentication.LinkedAccount;
import java.util.Optional;

public interface LinkedAccountRepository {
  LinkedAccount create(LinkedAccount newLinkedAccount);

  LinkedAccount update(LinkedAccount linkedAccountToUpdate);

  Optional<LinkedAccount> findByProviderAndProviderId(String provider, String providerId);

  void delete(LinkedAccount linkedAccount);
}
