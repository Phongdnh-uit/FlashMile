package com.uit.se356.core.application.upload.port.out;

import com.uit.se356.core.domain.entities.upload.File;

public interface FileRepository {
  File create(File file);

  File update(File file);
}
