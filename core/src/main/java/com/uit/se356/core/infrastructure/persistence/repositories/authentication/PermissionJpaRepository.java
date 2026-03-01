package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.PermissionJpaEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionJpaRepository extends CommonRepository<PermissionJpaEntity, String> {
  @Query("select p.id from PermissionJpaEntity p where p.id in :ids")
  Set<String> findExistingIds(@Param("ids") Set<String> ids);
}
