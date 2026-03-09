package com.uit.se356.core.infrastructure.config;

import com.uit.se356.common.security.PermissionScanner;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.area.handler.*;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.application.area.port.WardRepository;
import com.uit.se356.core.application.authentication.handler.LoginQueryHandler;
import com.uit.se356.core.application.authentication.handler.LogoutHandler;
import com.uit.se356.core.application.authentication.handler.OAuth2LoginCommandHandler;
import com.uit.se356.core.application.authentication.handler.ProcessVerificationHandler;
import com.uit.se356.core.application.authentication.handler.RegisterCommandHandler;
import com.uit.se356.core.application.authentication.handler.ResetPasswordCommandHandler;
import com.uit.se356.core.application.authentication.handler.SendVerificationCodeHandler;
import com.uit.se356.core.application.authentication.handler.TokenRotationHandler;
import com.uit.se356.core.application.authentication.handler.permission.AssignPermissionHandler;
import com.uit.se356.core.application.authentication.handler.permission.PermissionSummaryQueryHandler;
import com.uit.se356.core.application.authentication.handler.role.CreateRoleHandler;
import com.uit.se356.core.application.authentication.handler.role.DeleteRoleHandler;
import com.uit.se356.core.application.authentication.handler.role.RoleSummaryQueryHandler;
import com.uit.se356.core.application.authentication.handler.role.UpdateRoleHandler;
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
import com.uit.se356.core.application.contact.handler.CreateContactHandler;
import com.uit.se356.core.application.contact.handler.DeleteContactHandler;
import com.uit.se356.core.application.contact.handler.GetContactByPhoneHandler;
import com.uit.se356.core.application.contact.handler.GetMyContactsHandler;
import com.uit.se356.core.application.contact.handler.UpdateContactHandler;
import com.uit.se356.core.application.contact.port.RecipientContactRepository;
import com.uit.se356.core.application.internal.handler.DebugOtpHandler;
import com.uit.se356.core.application.internal.handler.SyncPermissionHandler;
import com.uit.se356.core.application.upload.handler.ConfirmUploadCommandHandler;
import com.uit.se356.core.application.upload.handler.UploadPresignedUrlHandler;
import com.uit.se356.core.application.upload.port.in.FileCleanupService;
import com.uit.se356.core.application.upload.port.out.FileRepository;
import com.uit.se356.core.application.upload.port.out.StorageProvider;
import com.uit.se356.core.application.upload.services.FileCleanupServiceImpl;
import com.uit.se356.core.application.upload.strategies.upload.AvatarUploadPolicy;
import com.uit.se356.core.application.upload.strategies.upload.UploadPolicy;
import com.uit.se356.core.application.user.handler.GetUserProfileHandler;
import com.uit.se356.core.application.user.handler.UpdateUserProfileHandler;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

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

  @Bean
  QueryHandler<?, ?> getUserProfileHandler(UserRepository userRepository) {
    return new GetUserProfileHandler(userRepository);
  }

  @Bean
  CommandHandler<?, ?> updateUserProfileHandler(UserRepository userRepository) {
    return new UpdateUserProfileHandler(userRepository);
  }

  @Bean
  CommandHandler<?, ?> createRoleCommandHandler(
      RoleRepository roleRepository, IdGenerator idGenerator) {
    return new CreateRoleHandler(roleRepository, idGenerator);
  }

  @Bean
  CommandHandler<?, ?> updateRoleCommandHandler(RoleRepository roleRepository) {
    return new UpdateRoleHandler(roleRepository);
  }

  @Bean
  CommandHandler<?, ?> deleteRoleCommandHandler(
      RoleRepository roleRepository, UserRepository userRepository) {
    return new DeleteRoleHandler(roleRepository, userRepository);
  }

  @Bean
  QueryHandler<?, ?> roleSummaryQueryHandler(RoleRepository roleRepository) {
    return new RoleSummaryQueryHandler(roleRepository);
  }

  @Bean
  QueryHandler<?, ?> permissionSummaryQueryHandler(PermissionRepository permissionRepository) {
    return new PermissionSummaryQueryHandler(permissionRepository);
  }

  @Bean
  CommandHandler<?, ?> assignPermissionHandler(
      RoleRepository roleRepository, PermissionRepository permissionRepository) {
    return new AssignPermissionHandler(roleRepository, permissionRepository);
  }

  @Bean
  CommandHandler<?, ?> createContactCommandHandler(
      RecipientContactRepository contactRepository, IdGenerator idGenerator) {
    return new CreateContactHandler(contactRepository, idGenerator);
  }

  @Bean
  CommandHandler<?, ?> updateContactCommandHandler(RecipientContactRepository contactRepository) {
    return new UpdateContactHandler(contactRepository);
  }

  @Bean
  CommandHandler<?, ?> deleteContactCommandHandler(RecipientContactRepository contactRepository) {
    return new DeleteContactHandler(contactRepository);
  }

  @Bean
  QueryHandler<?, ?> getContactByPhoneHandler(RecipientContactRepository contactRepository) {
    return new GetContactByPhoneHandler(contactRepository);
  }

  @Bean
  QueryHandler<?, ?> getMyContactsHandler(RecipientContactRepository contactRepository) {
    return new GetMyContactsHandler(contactRepository);
  }

  @Bean
  CommandHandler<?, ?> createProvinceHandler(
      ProvinceRepository provinceRepository, IdGenerator idGenerator) {
    return new CreateProvinceHandler(provinceRepository, idGenerator);
  }

  @Bean
  CommandHandler<?, ?> updateProvinceHandler(ProvinceRepository provinceRepository) {
    return new UpdateProvinceHandler(provinceRepository);
  }

  @Bean
  CommandHandler<?, ?> deleteProvinceHandler(ProvinceRepository provinceRepository) {
    return new DeleteProvinceHandler(provinceRepository);
  }

  @Bean
  QueryHandler<?, ?> provinceSummaryQueryHandler(ProvinceRepository provinceRepository) {
    return new ProvinceSummaryQueryHandler(provinceRepository);
  }

  @Bean
  CommandHandler<?, ?> createWardHandler(
      WardRepository wardRepository,
      ProvinceRepository provinceRepository,
      IdGenerator idGenerator) {
    return new CreateWardHandler(wardRepository, provinceRepository, idGenerator);
  }

  @Bean
  CommandHandler<?, ?> updateWardHandler(
      WardRepository wardRepository, ProvinceRepository provinceRepository) {
    return new UpdateWardHandler(wardRepository, provinceRepository);
  }

  @Bean
  CommandHandler<?, ?> deleteWardHandler(WardRepository wardRepository) {
    return new DeleteWardHandler(wardRepository);
  }

  @Bean
  QueryHandler<?, ?> wardSummaryQueryHandler(WardRepository wardRepository) {
    return new WardSummaryQueryHandler(wardRepository);
  }

  @Bean
  CommandHandler<?, ?> importProvinceGeoJsonHandler(
      ProvinceRepository repo, ObjectMapper mapper, IdGenerator idGenerator) {
    return new ImportProvinceGeoJsonHandler(repo, mapper, idGenerator);
  }

  @Bean
  CommandHandler<?, ?> importWardGeoJsonHandler(
      ProvinceRepository provinceRepository,
      WardRepository wardRepository,
      ObjectMapper objectMapper,
      IdGenerator idGenerator) {
    return new ImportWardGeoJsonHandler(
        provinceRepository, wardRepository, objectMapper, idGenerator);
  }

  @Bean
  UploadPolicy avatarUploadPolicy() {
    return new AvatarUploadPolicy();
  }

  @Bean
  CommandHandler<?, ?> uploadPresignedUrlCommandHandler(
      List<UploadPolicy> uploadPolicies,
      IdGenerator idGenerator,
      StorageProvider storageProvider,
      FileRepository fileRepository) {
    return new UploadPresignedUrlHandler(
        fileRepository, uploadPolicies, idGenerator, storageProvider);
  }

  @Bean
  CommandHandler<?, ?> confirmUploadCommandHandler(
      FileRepository fileRepository, StorageProvider storageProvider) {
    return new ConfirmUploadCommandHandler(fileRepository, storageProvider);
  }

  @Bean
  FileCleanupService fileCleanupService(
      FileRepository fileRepository, StorageProvider storageProvider) {
    return new FileCleanupServiceImpl(fileRepository, storageProvider);
  }
}
