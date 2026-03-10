package com.uit.se356.core.infrastructure.job;

import com.uit.se356.core.application.authentication.port.out.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class VerificationCleanup {
  private final VerificationRepository verificationRepository;

  @Scheduled(cron = "0 0 2 * * ?") // Chạy lúc 2 giờ sáng hàng ngày
  @Transactional
  public void cleanupExpiredVerifications() {
    log.info("Starting cleanup of expired verifications");
    verificationRepository.cleanupExpiredVerifications();
    log.info("Finished cleanup of expired verifications");
  }
}
