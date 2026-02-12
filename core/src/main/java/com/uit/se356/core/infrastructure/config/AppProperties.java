package com.uit.se356.core.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
public class AppProperties {

  private Security security;
  private Mail mail;
  private Frontend frontend;
  private Verification verification;

  public Security getSecurity() {
    return security;
  }

  public void setSecurity(Security security) {
    this.security = security;
  }

  public Mail getMail() {
    return mail;
  }

  public void setMail(Mail mail) {
    this.mail = mail;
  }

  public Verification getVerification() {
    return verification;
  }

  public void setVerification(Verification verification) {
    this.verification = verification;
  }

  @Getter
  @Setter
  public static class Verification {
    private long emailLinkExpiration;
    private long smsOtpExpiration;
    private long phoneVerifiedTokenExpiration;
  }

  public static class Security {
    private Jwt jwt;

    public Jwt getJwt() {
      return jwt;
    }

    public void setJwt(Jwt jwt) {
      this.jwt = jwt;
    }
  }

  @Getter
  @Setter
  public static class Jwt {
    private String secretKey;
    private long expiration;
    private long refreshExpiration;
  }

  @Getter
  @Setter
  public static class Mail {
    private String from;
  }

  @Getter
  @Setter
  public static class Frontend {
    private String baseUrl;
    private String verifyEmailPath;
  }

  public Frontend getFrontend() {
    return frontend;
  }

  public void setFrontend(Frontend frontend) {
    this.frontend = frontend;
  }
}
