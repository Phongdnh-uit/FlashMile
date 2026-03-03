package com.uit.se356.core.application.upload.port.out;

import com.uit.se356.core.domain.entities.upload.File;
import java.util.Optional;

public interface FileRepository {
  File create(File file);

  File update(File file);

  Optional<File> findByStorageKey(String storageKey);
}
