package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.command.OAuth2LoginCommand;
import com.uit.se356.core.application.authentication.port.LinkedAccountRepository;
import com.uit.se356.core.application.authentication.port.RoleRepository;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.LinkedAccount;
import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.LinkedAccountId;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public class OAuth2LoginCommandHandler implements CommandHandler<OAuth2LoginCommand, User> {
  private final LinkedAccountRepository linkedAccountRepository;
  private final UserRepository userRepository;
  private final IdGenerator idGenerator;
  private final RoleRepository roleRepository;

  public OAuth2LoginCommandHandler(
      LinkedAccountRepository linkedAccountRepository,
      UserRepository userRepository,
      IdGenerator idGenerator,
      RoleRepository roleRepository) {
    this.linkedAccountRepository = linkedAccountRepository;
    this.userRepository = userRepository;
    this.idGenerator = idGenerator;
    this.roleRepository = roleRepository;
  }

  @Transactional
  @Override
  public User handle(OAuth2LoginCommand command) {
    Optional<LinkedAccount> linkedAccount =
        linkedAccountRepository.findByProviderAndProviderId(
            command.provider(), command.providerUserId());
    // Trường hợp đã có tài khoản liên kết, trả về user tương ứng
    if (linkedAccount.isPresent()) {
      return userRepository
          .findById(linkedAccount.get().getUserId())
          .orElseThrow(() -> new AppException(AuthErrorCode.INVALID_CREDENTIALS));
    }

    // Trường hợp chưa có tài khoản liên kết, tạo mới user và liên kết
    // Check lại phone
    if (command.verifiedPhone() == null || command.verifiedPhone().isEmpty()) {
      throw new AppException(AuthErrorCode.INVALID_CREDENTIALS);
    }
    // Không cần check lại vì bước xác thực đã đảm bảo số điện thoại hợp lệ và thuộc về người dùng
    // Kiểm tra xem email có tồn tại không, nếu có thì báo lỗi vì email đã được sử dụng
    PhoneNumber phoneNumber = new PhoneNumber(command.verifiedPhone());
    Email email = new Email(command.email());
    UserId userId = new UserId(idGenerator.generate().toString());
    if (userRepository.existsByEmail(email)) {
      throw new AppException(AuthErrorCode.EMAIL_ALREADY_USED);
    }
    // Lấy vai trò mặc định
    Role defaultRole =
        roleRepository
            .findDefault()
            .orElseThrow(() -> new AppException(AuthErrorCode.ROLE_NOT_FOUND));
    User newUser =
        User.createOAuthUser(userId, command.fullName(), email, phoneNumber, defaultRole.getId());
    newUser = userRepository.save(newUser);

    // Tạo liên kết tài khoản
    LinkedAccount newLinkedAccount =
        LinkedAccount.create(
            new LinkedAccountId(idGenerator.generate().toString()),
            userId,
            command.provider(),
            command.providerUserId());
    linkedAccountRepository.save(newLinkedAccount);
    // Trả về user mới tạo
    return newUser;
  }
}
