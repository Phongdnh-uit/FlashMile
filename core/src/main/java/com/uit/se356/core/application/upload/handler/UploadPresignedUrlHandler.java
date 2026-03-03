package com.uit.se356.core.application.upload.handler;

import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.upload.command.UploadPresignedUrlCommand;
import com.uit.se356.core.application.upload.port.out.FileRepository;
import com.uit.se356.core.application.upload.port.out.StorageProvider;
import com.uit.se356.core.application.upload.result.PresignedUrlResult;
import com.uit.se356.core.application.upload.strategies.UploadPolicy;
import com.uit.se356.core.domain.entities.upload.File;
import com.uit.se356.core.domain.vo.upload.FileId;
import com.uit.se356.core.domain.vo.upload.UploadType;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UploadPresignedUrlHandler
    implements CommandHandler<UploadPresignedUrlCommand, PresignedUrlResult> {

  private final FileRepository fileRepository;
  private final StorageProvider storageProvider;
  private final Map<UploadType, UploadPolicy> uploadPolicies;
  private final IdGenerator idGenerator;

  public UploadPresignedUrlHandler(
      FileRepository fileRepository,
      List<UploadPolicy> uploadPolicies,
      IdGenerator idGenerator,
      StorageProvider storageProvider) {
    this.fileRepository = fileRepository;
    this.uploadPolicies =
        uploadPolicies.stream().collect(Collectors.toMap(UploadPolicy::type, policy -> policy));
    this.idGenerator = idGenerator;
    this.storageProvider = storageProvider;
  }

  @Override
  public PresignedUrlResult handle(UploadPresignedUrlCommand command) {
    // Dùng từng policy theo từng loại để kiểm tra tính hợp lệ của size và content type
    UploadPolicy policy = uploadPolicies.get(command.type());
    policy.validate(command);

    String storageKey = idGenerator.generate().toString();

    File file =
        File.create(
            new FileId(idGenerator.generate().toString()),
            storageKey,
            command.fileName(),
            command.contentType(),
            command.fileSize());

    fileRepository.create(file);

    return storageProvider.generatePutPresignedUrl(storageKey, Duration.ofMinutes(5));
  }
}
