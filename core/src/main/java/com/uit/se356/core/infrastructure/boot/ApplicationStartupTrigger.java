package com.uit.se356.core.infrastructure.boot;

import com.uit.se356.core.application.seeding.DataSeedingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * This class acts as an "adapter" between the Spring Boot framework's lifecycle and our
 * application's pure business logic. Its only job is to trigger application-layer services upon
 * startup.
 */
@Component
@RequiredArgsConstructor
public class ApplicationStartupTrigger implements ApplicationRunner {

  private final DataSeedingService dataSeedingService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    dataSeedingService.seedData();
  }
}
