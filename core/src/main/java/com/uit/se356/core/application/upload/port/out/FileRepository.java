package com.uit.se356.core.application.upload.port.out;

import com.uit.se356.core.application.upload.projections.BasicFileProjection;
import com.uit.se356.core.domain.entities.upload.File;
import com.uit.se356.core.domain.vo.upload.FileId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface FileRepository {
  File create(File file);

  File update(File file);

  Optional<File> findByStorageKey(String storageKey);

  Optional<BasicFileProjection> findBasicById(FileId id);

  List<BasicFileProjection> findAllByCleanupCondition(Instant createdBefore);

  void deleteAllByIds(List<FileId> ids);
}
