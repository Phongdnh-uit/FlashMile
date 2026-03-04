package com.uit.se356.core.application.upload.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.upload.command.ConfirmUploadCommand;
import com.uit.se356.core.application.upload.port.out.FileRepository;
import com.uit.se356.core.application.upload.port.out.StorageProvider;
import com.uit.se356.core.domain.vo.upload.FileStatus;

public class ConfirmUploadCommandHandler implements CommandHandler<ConfirmUploadCommand, Void> {
  private final FileRepository fileRepository;
  private final StorageProvider storageProvider;

  public ConfirmUploadCommandHandler(
      FileRepository fileRepository, StorageProvider storageProvider) {
    this.fileRepository = fileRepository;
    this.storageProvider = storageProvider;
  }

  @Override
  public Void handle(ConfirmUploadCommand command) {
    // 1. Lấy file từ repository với objectKey tương ứng
    var file =
        fileRepository
            .findByStorageKey(command.storageKey())
            .orElseThrow(() -> new AppException(CommonErrorCode.RESOURCE_NOT_FOUND));

    // Kiểm tra nếu như mà file đã có trạng thái UPLOADED rồi thì thôi, có thể là do client gọi lại
    // nhiều lần, hoặc do file đã được xác nhận rồi
    if (file.getStatus() == FileStatus.UPLOADED) {
      return null;
    }

    var stat = storageProvider.getQuarantineObjectStat(command.storageKey());

    // Technical note: Không cần check lại size và content type

    // boolean isMatching =
    //     file.getSize().equals(stat.size())
    //         && file.getContentType().equalsIgnoreCase(stat.contentType());
    //
    // if (!isMatching) {
    //   // Chỉ đánh dấu lại là invalid để job xóa db record sau này, bên storage tự cấu hình ttl để
    //   // xóa file sau một khoảng thời gian nhất định
    //   file.markAsInvalid();
    //   fileRepository.update(file);
    //   throw new AppException(UploadErrorCode.INVALID_FILE);
    // }

    // Nếu đúng thì gọi promoteFromQuarantine để cập nhật vào bucket main
    storageProvider.promoteFromQuarantine(command.storageKey());

    // Sau đó gọi markAsUploaded rồi lưu xuống database
    file.markAsUploaded();
    fileRepository.update(file);

    return null;
  }
}
