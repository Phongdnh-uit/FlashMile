package com.uit.se356.core.infrastructure.repositories.upload;

import com.uit.se356.core.application.upload.port.out.FileRepository;
import com.uit.se356.core.application.upload.projections.BasicFileProjection;
import com.uit.se356.core.domain.entities.upload.File;
import com.uit.se356.core.domain.vo.upload.FileId;
import com.uit.se356.core.domain.vo.upload.FileStatus;
import com.uit.se356.core.infrastructure.persistence.entities.upload.FileJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.upload.FilePersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.upload.FileJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
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

  @Override
  public void deleteAllByIds(List<FileId> ids) {
    fileJpaRepository.deleteAllById(ids.stream().map(id -> id.value()).toList());
  }

  @Override
  public Optional<BasicFileProjection> findBasicById(FileId id) {
    return fileJpaRepository.findBy(
        (root, query, builder) -> builder.equal(root.get("id"), id.value()),
        q -> q.as(BasicFileProjection.class).one());
  }

  @Override
  public List<BasicFileProjection> findAllByCleanupCondition(Instant createdBefore) {
    // Lấy tất cả trạng thái trừ UPLOADED, nếu là PENDING thì dới khoảng 1h
    return fileJpaRepository.findBy(
        (root, query, builder) ->
            builder.or(
                builder.and(
                    builder.equal(root.get("status"), FileStatus.PENDING),
                    builder.lessThan(root.get("createdAt"), createdBefore.minusSeconds(60 * 60))),
                builder.and(
                    builder.notEqual(root.get("status"), FileStatus.UPLOADED),
                    builder.lessThan(root.get("createdAt"), createdBefore))),
        q -> q.as(BasicFileProjection.class).all());
  }
}
