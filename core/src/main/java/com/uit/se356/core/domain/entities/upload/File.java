package com.uit.se356.core.domain.entities.upload;

import com.uit.se356.core.domain.vo.upload.FileId;
import com.uit.se356.core.domain.vo.upload.FileStatus;

public class File {
  private final FileId id;
  private String storageKey;
  private String originalName;
  private String contentType;
  private Long size;
  private FileStatus status;

  private File(
      FileId id,
      String storageKey,
      String originalName,
      String contentType,
      Long size,
      FileStatus status) {
    this.id = id;
    this.storageKey = storageKey;
    this.originalName = originalName;
    this.contentType = contentType;
    this.size = size;
    this.status = status;
  }

  // ============================ FACTORIES ============================
  public static File create(
      FileId id, String storageKey, String originalName, String contentType, Long size) {
    return new File(id, storageKey, originalName, contentType, size, FileStatus.PENDING);
  }

  public static File rehydrate(
      FileId id,
      String storageKey,
      String originalName,
      String contentType,
      Long size,
      FileStatus status) {
    return new File(id, storageKey, originalName, contentType, size, status);
  }

  // ============================ BEHAVIOURS ============================
  public void markAsUploaded() {
    this.status = FileStatus.UPLOADED;
  }

  public void markAsInvalid() {
    this.status = FileStatus.INVALID;
  }

  // ============================ GETTERS ============================
  public FileId getId() {
    return id;
  }

  public String getStorageKey() {
    return storageKey;
  }

  public String getOriginalName() {
    return originalName;
  }

  public String getContentType() {
    return contentType;
  }

  public Long getSize() {
    return size;
  }

  public FileStatus getStatus() {
    return status;
  }
}
