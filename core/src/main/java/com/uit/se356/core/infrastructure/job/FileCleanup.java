package com.uit.se356.core.infrastructure.job;

import com.uit.se356.core.application.upload.port.in.FileCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileCleanup {
  private final FileCleanupService fileCleanupService;

  @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
  public void execute() {
    log.info("Starting file cleanup job...");
    try {
      fileCleanupService.cleanup();
      log.info("File cleanup job completed successfully.");
    } catch (Exception e) {
      log.error("Error during file cleanup job: ", e);
    }
  }
}
