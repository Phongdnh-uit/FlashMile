package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RoleJpaEntity;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends CommonRepository<RoleJpaEntity, String> {

  @EntityGraph(attributePaths = {"permissions"})
  @Override
  Optional<RoleJpaEntity> findById(String id);

  @EntityGraph(attributePaths = {"permissions"})
  @Override
  <S extends RoleJpaEntity> Optional<S> findOne(Example<S> example);
}
