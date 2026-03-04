package com.uit.se356.core.infrastructure.persistence.repositories.upload;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.upload.FileJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface FileJpaRepository extends CommonRepository<FileJpaEntity, String> {}
