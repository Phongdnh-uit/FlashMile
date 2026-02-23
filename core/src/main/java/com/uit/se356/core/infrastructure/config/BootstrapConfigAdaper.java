package com.uit.se356.core.infrastructure.config;

import com.uit.se356.core.application.seeding.port.BootstrapConfigPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BootstrapConfigAdaper implements BootstrapConfigPort {
  private final AppProperties appProperties;

  @Override
  public String getAdminFullName() {
    return appProperties.getBootstrap().getAdminFullName();
  }

  @Override
  public String getAdminEmail() {
    return appProperties.getBootstrap().getAdminEmail();
  }

  @Override
  public String getAdminPhoneNumber() {
    return appProperties.getBootstrap().getAdminPhoneNumber();
  }

  @Override
  public String getAdminPassword() {
    return appProperties.getBootstrap().getAdminPassword();
  }
}
