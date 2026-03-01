package com.uit.se356.core.infrastructure.persistence.repositories.contact;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.contact.RecipientContactJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientContactJpaRepository
    extends CommonRepository<RecipientContactJpaEntity, String> {
  List<RecipientContactJpaEntity> findByUserIdOrderByNameAsc(String userId);

  Optional<RecipientContactJpaEntity> findByUserIdAndPhoneNumber(String userId, String phoneNumber);

  boolean existsByUserIdAndPhoneNumber(String userId, String phoneNumber);
}
