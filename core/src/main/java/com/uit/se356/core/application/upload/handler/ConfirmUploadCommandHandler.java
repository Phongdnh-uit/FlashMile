package com.uit.se356.core.application.upload.handler;

import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.upload.command.ConfirmUploadCommand;
import com.uit.se356.core.application.upload.port.out.FileRepository;
import com.uit.se356.core.application.upload.port.out.StorageProvider;
import com.uit.se356.core.domain.vo.upload.FileStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ConfirmUploadCommandHandler implements CommandHandler<ConfirmUploadCommand, Void> {
  private final FileRepository fileRepository;
  private final StorageProvider storageProvider;

  @Override
  public Void handle(ConfirmUploadCommand command) {
    // Lấy file từ repository với objectKey tương ứng
    var file =
        fileRepository
            .findByStorageKey(command.storageKey())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "File not found with storage key: " + command.storageKey()));

    // Kiểm tra nếu như mà file đã có trạng thái UPLOADED rồi thì thôi
    if (file.getStatus() == FileStatus.UPLOADED) {
      log.info("File with storage key {} is already uploaded.", command.storageKey());
      return null;
    }

    // Kiểm tra size, content-type lưu trong db có khớp với minio trả về không
    boolean isMatching =
        file.getSize().equals(command.size())
            && file.getContentType().equalsIgnoreCase(command.contentType());

    if (!isMatching) {
      // Không thì đánh dấu và không an toàn
      log.warn(
          "File with storage key {} has mismatched metadata. DB: size={}, contentType={}. Minio: size={}, contentType={}",
          command.storageKey(),
          file.getSize(),
          file.getContentType(),
          command.size(),
          command.contentType());
      file.markAsInvalid();
      fileRepository.update(file);
      return null;
    }

    // Nếu đúng thì gọi promoteFromQuarantine để cập nhật vào bucket main
    storageProvider.promoteFromQuarantine(command.storageKey());

    // Sau đó gọi markAsUploaded rồi lưu xuống database
    file.markAsUploaded();
    fileRepository.update(file);

    log.info("File with storage key {} has been successfully uploaded.", command.storageKey());

    return null;
  }
}
