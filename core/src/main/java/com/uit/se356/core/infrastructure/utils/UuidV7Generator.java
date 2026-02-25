package com.uit.se356.core.infrastructure.utils;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import com.uit.se356.common.utils.IdGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UuidV7Generator implements IdGenerator {
  private final TimeBasedEpochGenerator timeBasedEpochGenerator =
      Generators.timeBasedEpochGenerator();

  @Override
  public UUID generate() {
    return timeBasedEpochGenerator.generate();
  }
}
