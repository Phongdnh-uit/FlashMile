package com.uit.se356.core.application.upload.services;

import com.uit.se356.core.application.upload.port.in.FileCleanupService;
import com.uit.se356.core.application.upload.port.out.FileRepository;
import com.uit.se356.core.application.upload.port.out.StorageProvider;
import com.uit.se356.core.application.upload.projections.BasicFileProjection;
import com.uit.se356.core.domain.vo.upload.FileId;
import com.uit.se356.core.domain.vo.upload.FileStatus;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class FileCleanupServiceImpl implements FileCleanupService {
  private final FileRepository fileRepository;
  private final StorageProvider storageProvider;

  public FileCleanupServiceImpl(FileRepository fileRepository, StorageProvider storageProvider) {
    this.fileRepository = fileRepository;
    this.storageProvider = storageProvider;
  }

  @Override
  public void cleanup() {
    /**
     * Các trạng thái cần cleanup là PENDING và DELETED, với trạng thái INVALID Với trạng thái
     * PENDING do người dùng chưa hoàn thành việc upload nên sẽ xóa sau 15 phút Với trạng thái
     * DELETED cần xóa db record và file trên storage, tuy nhiên do có thể có lỗi xảy ra nên sẽ xóa
     * file trên storage trước rồi mới xóa db record Với trạng thái INVALID sẽ không cần gọi storage
     * để xóa vì storage tự cấu hình ttl để xóa file
     */
    var candidates = fileRepository.findAllByCleanupCondition(Instant.now());

    var byStatus =
        candidates.stream().collect(Collectors.groupingBy(BasicFileProjection::getStatus));

    // Xóa file trên storage trước rồi mới xóa db record
    byStatus
        .getOrDefault(FileStatus.DELETED, List.of())
        .forEach(file -> storageProvider.delete(file.getStorageKey()));

    // Xóa db record
    fileRepository.deleteAllByIds(
        candidates.stream().map(v -> new FileId(v.getId())).collect(Collectors.toList()));
  }
}
