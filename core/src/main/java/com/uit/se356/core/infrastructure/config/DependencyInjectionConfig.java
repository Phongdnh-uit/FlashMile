package com.uit.se356.core.infrastructure.config;

import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.handler.IssueTokenService;
import com.uit.se356.core.application.authentication.handler.LoginQueryHandler;
import com.uit.se356.core.application.authentication.handler.OAuth2LoginCommandHandler;
import com.uit.se356.core.application.authentication.handler.ProcessVerificationHandler;
import com.uit.se356.core.application.authentication.handler.RegisterCommandHandler;
import com.uit.se356.core.application.authentication.handler.ResetPasswordCommandHandler;
import com.uit.se356.core.application.authentication.handler.SendVerificationCodeHandler;
import com.uit.se356.core.application.authentication.handler.TokenRotationHandler;
import com.uit.se356.core.application.authentication.port.CacheRepository;
import com.uit.se356.core.application.authentication.port.LinkedAccountRepository;
import com.uit.se356.core.application.authentication.port.PasswordEncoder;
import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.application.authentication.port.RoleRepository;
import com.uit.se356.core.application.authentication.port.TokenProvider;
import com.uit.se356.core.application.authentication.port.VerificationConfigPort;
import com.uit.se356.core.application.authentication.port.VerificationRepository;
import com.uit.se356.core.application.authentication.port.VerificationSender;
import com.uit.se356.core.application.authentication.strategies.verification.process.EmailVerificationProcessingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.process.PhoneVerificationProcessingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.process.ProcessVerificationStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.send.EmailVerificationSendingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.send.ForgotPasswordSendingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.send.PhoneVerificationSendingStrategy;
import com.uit.se356.core.application.authentication.strategies.verification.send.SendVerificationStrategy;
import com.uit.se356.core.application.user.port.UserRepository;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Cấu hình các bean cho hệ thống, chủ yếu từ tầng application để decoupling với framework */
@Configuration
public class DependencyInjectionConfig {
  @Bean
  PhoneVerificationSendingStrategy phoneVerificationSendingStrategy(
      UserRepository userRepository,
      CacheRepository cacheRepository,
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
      CacheRepository cacheRepository, VerificationConfigPort verificationConfigPort) {
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
      CacheRepository cacheRepository,
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
    return new IssueTokenService(tokenProvider, refreshTokenRepository, idGenerator);
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
      RefreshTokenRepository refreshTokenRepository, IssueTokenService issueTokenHander) {
    return new TokenRotationHandler(refreshTokenRepository, issueTokenHander);
  }

  @Bean
  CommandHandler<?, ?> resetPasswordCommandHandler(
      VerificationRepository verificationRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    return new ResetPasswordCommandHandler(verificationRepository, userRepository, passwordEncoder);
  }
}
