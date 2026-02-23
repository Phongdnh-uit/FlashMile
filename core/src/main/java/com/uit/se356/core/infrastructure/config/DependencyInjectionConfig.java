package com.uit.se356.core.infrastructure.config;

import com.uit.se356.common.security.PermissionScanner;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.authentication.handler.LoginQueryHandler;
import com.uit.se356.core.application.authentication.handler.LogoutHandler;
import com.uit.se356.core.application.authentication.handler.OAuth2LoginCommandHandler;
import com.uit.se356.core.application.authentication.handler.ProcessVerificationHandler;
import com.uit.se356.core.application.authentication.handler.RegisterCommandHandler;
import com.uit.se356.core.application.authentication.handler.ResetPasswordCommandHandler;
import com.uit.se356.core.application.authentication.handler.SendVerificationCodeHandler;
import com.uit.se356.core.application.authentication.handler.TokenRotationHandler;
import com.uit.se356.core.application.authentication.port.in.IssueTokenService;
import com.uit.se356.core.application.authentication.port.in.PermissionChecker;
import com.uit.se356.core.application.authentication.port.out.AuthCacheRepository;
import com.uit.se356.core.application.authentication.port.out.LinkedAccountRepository;
import com.uit.se356.core.application.authentication.port.out.PasswordEncoder;
import com.uit.se356.core.application.authentication.port.out.PermissionRepository;
import com.uit.se356.core.application.authentication.port.out.RefreshTokenRepository;
import com.uit.se356.core.application.authentication.port.out.RoleRepository;
import com.uit.se356.core.application.authentication.port.out.TokenProvider;
import com.uit.se356.core.application.authentication.port.out.VerificationConfigPort;
import com.uit.se356.core.application.authentication.port.out.VerificationRepository;
import com.uit.se356.core.application.authentication.port.out.VerificationSender;
import com.uit.se356.core.application.authentication.services.IssueTokenServiceImpl;
import com.uit.se356.core.application.authentication.services.PermissionCheckerImpl;
import com.uit.se356.core.application.authentication.strategies.verification.process.EmailVerificationProcessingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.process.PhoneVerificationProcessingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.process.ProcessVerificationStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.send.EmailVerificationSendingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.send.ForgotPasswordSendingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.send.PhoneVerificationSendingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.send.SendVerificationStrategy;
import com.uit.se356.core.application.internal.handler.DebugOtpHandler;
import com.uit.se356.core.application.internal.handler.SyncPermissionHandler;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Cấu hình các bean cho hệ thống, chủ yếu từ tầng application để decoupling với framework */
@Configuration
public class DependencyInjectionConfig {
  @Bean
  PhoneVerificationSendingStrategy phoneVerificationSendingStrategy(
      UserRepository userRepository,
      AuthCacheRepository cacheRepository,
      VerificationConfigPort verificationConfigPort) {
    return new PhoneVerificationSendingStrategy(
        userRepository, cacheRepository, verificationConfigPort);
  }

  @Bean
  EmailVerificationSendingStrategy emailVerificationSendingStrategy(
      IdGenerator idGenerator,
      UserRepository userRepository,
      VerificationRepository verificationRepository,
      VerificationConfigPort verificationConfigPort) {
    return new EmailVerificationSendingStrategy(
        userRepository, verificationRepository, idGenerator, verificationConfigPort);
  }

  @Bean
  ForgotPasswordSendingStrategy forgotPasswordSendingStrategy(
      IdGenerator idGenerator,
      UserRepository userRepository,
      VerificationRepository verificationRepository,
      VerificationConfigPort verificationConfigPort) {
    return new ForgotPasswordSendingStrategy(
        userRepository, verificationRepository, verificationConfigPort, idGenerator);
  }

  @Bean
  PhoneVerificationProcessingStrategy phoneVerificationProcessingStrategy(
      AuthCacheRepository cacheRepository, VerificationConfigPort verificationConfigPort) {
    return new PhoneVerificationProcessingStrategy(cacheRepository, verificationConfigPort);
  }

  @Bean
  EmailVerificationProcessingStrategy emailVerificationProcessingStrategy(
      UserRepository userRepository, VerificationRepository verificationRepository) {
    return new EmailVerificationProcessingStrategy(userRepository, verificationRepository);
  }

  @Bean
  QueryHandler<?, ?> sendVerificationCodeHandler(
      List<SendVerificationStrategy> strategies, List<VerificationSender> senders) {
    return new SendVerificationCodeHandler(strategies, senders);
  }

  @Bean
  QueryHandler<?, ?> processVerificationHandler(List<ProcessVerificationStrategy> strategies) {
    return new ProcessVerificationHandler(strategies);
  }

  @Bean
  CommandHandler<?, ?> registerCommandHandler(
      AuthCacheRepository cacheRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      IdGenerator idGenerator,
      QueryBus queryBus,
      RoleRepository roleRepository) {
    return new RegisterCommandHandler(
        cacheRepository, userRepository, passwordEncoder, idGenerator, queryBus, roleRepository);
  }

  @Bean
  CommandHandler<?, ?> oAuth2LoginCommandHandler(
      LinkedAccountRepository linkedAccountRepository,
      UserRepository userRepository,
      IdGenerator idGenerator,
      RoleRepository roleRepository) {
    return new OAuth2LoginCommandHandler(
        linkedAccountRepository, userRepository, idGenerator, roleRepository);
  }

  @Bean
  IssueTokenService issueTokenHander(
      TokenProvider tokenProvider,
      RefreshTokenRepository refreshTokenRepository,
      IdGenerator idGenerator) {
    return new IssueTokenServiceImpl(tokenProvider, refreshTokenRepository, idGenerator);
  }

  @Bean
  QueryHandler<?, ?> loginQueryHandler(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      IssueTokenService issueTokenService) {
    return new LoginQueryHandler(userRepository, passwordEncoder, issueTokenService);
  }

  @Bean
  CommandHandler<?, ?> tokenRotationHandler(
      RefreshTokenRepository refreshTokenRepository,
      IssueTokenService issueTokenHander,
      UserRepository userRepository) {
    return new TokenRotationHandler(refreshTokenRepository, issueTokenHander, userRepository);
  }

  @Bean
  CommandHandler<?, ?> resetPasswordCommandHandler(
      VerificationRepository verificationRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    return new ResetPasswordCommandHandler(verificationRepository, userRepository, passwordEncoder);
  }

  @Bean
  CommandHandler<?, ?> syncPermissionCommandHandler(
      PermissionScanner permissionScanner,
      PermissionRepository permissionRepository,
      IdGenerator idGenerator) {
    return new SyncPermissionHandler(permissionScanner, permissionRepository, idGenerator);
  }

  @Bean
  QueryHandler<?, ?> debugOtpHandler(AuthCacheRepository cacheRepository) {
    return new DebugOtpHandler(cacheRepository);
  }

  @Bean
  CommandHandler<?, ?> logoutCommandHandler(
      RefreshTokenRepository refreshTokenRepository, TokenProvider tokenProvider) {
    return new LogoutHandler(refreshTokenRepository, tokenProvider);
  }

  @Bean
  PermissionChecker permissionChecker(
      PermissionRepository permissionRepository,
      AuthCacheRepository cacheRepository,
      SecurityUtil<UserId> securityUtil,
      RoleRepository roleRepository) {
    return new PermissionCheckerImpl(
        cacheRepository, permissionRepository, securityUtil, roleRepository);
  }
}
