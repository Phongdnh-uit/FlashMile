package com.uit.se356.core.infrastructure.job;

import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class RefreshTokenCleanup {
  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshTokenCleanup(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  private final long CLEANUP_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24ha - Hardcode tạm.

  @Scheduled(cron = "0 0 2 * * ?") // Chạy lúc 2 giờ sáng hàng ngày
  @Transactional
  public void cleanupExpiredRefreshTokens() {
    log.info("Starting cleanup of expired refresh tokens");
    refreshTokenRepository.cleanupExpiredTokens(CLEANUP_INTERVAL_MS);
    log.info("Finished cleanup of expired refresh tokens");
  }
}
