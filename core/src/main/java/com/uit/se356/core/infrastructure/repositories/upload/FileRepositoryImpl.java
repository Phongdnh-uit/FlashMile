package com.uit.se356.core.infrastructure.repositories.upload;

import com.uit.se356.core.application.upload.port.out.FileRepository;
import com.uit.se356.core.domain.entities.upload.File;
import com.uit.se356.core.infrastructure.persistence.entities.upload.FileJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.upload.FilePersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.upload.FileJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileRepositoryImpl implements FileRepository {

  private final FileJpaRepository fileJpaRepository;
  private final FilePersistenceMapper fileMapper;

  @Override
  @Transactional
  public File create(File file) {
    FileJpaEntity entityToCreate = fileMapper.toEntity(file);
    FileJpaEntity savedEntity = fileJpaRepository.save(entityToCreate);
    return fileMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public File update(File file) {
    FileJpaEntity existingEntity =
        fileJpaRepository
            .findById(file.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException("File not found with id: " + file.getId().value()));

    fileMapper.updateEntityFromDomain(file, existingEntity);
    return fileMapper.toDomain(existingEntity);
  }

  @Override
  public Optional<File> findByStorageKey(String storageKey) {
    return fileJpaRepository
        .findOne(
            (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("storageKey"), storageKey))
        .map(fileMapper::toDomain);
  }
}
