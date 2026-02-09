package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.UserErrorCode;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.UserStatus;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class User {
  private static final Map<UserStatus, Set<UserStatus>> allowedTransitions =
      Map.of(
          UserStatus.UNVERIFIED, Set.of(UserStatus.ACTIVE),
          UserStatus.ACTIVE, Set.of(UserStatus.DELETED, UserStatus.BLOCKED),
          UserStatus.BLOCKED, Set.of(UserStatus.ACTIVE));

  private final UserId userId;
  private String fullName;
  private PhoneNumber phoneNumber;
  private Email email;
  private String passwordHash;
  private boolean phoneVerified;
  private boolean emailVerified;
  private UserStatus status;
  private Instant createdAt;
  private Instant updatedAt;
  private UserId createdBy;
  private UserId updatedBy;

  // Hàm khởi tạo đầy đủ
  // Note: private nhằm chắc chắn User luôn ở trạng thái đúng
  private User(
      UserId userId,
      String fullName,
      Email email,
      String passwordHash,
      PhoneNumber phoneNumber,
      UserStatus status,
      boolean phoneVerified,
      boolean emailVerified,
      Instant createdAt,
      Instant updatedAt,
      UserId createdBy,
      UserId updatedBy) {
    this.userId = userId;
    this.fullName = fullName;
    this.email = email;
    this.passwordHash = passwordHash;
    this.phoneNumber = phoneNumber;
    this.phoneVerified = phoneVerified;
    this.emailVerified = emailVerified;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
  }

  // ============================ FACTORY ============================
  public static User create(
      UserId userId,
      String fullName,
      Email email,
      String passwordHash,
      PhoneNumber phoneNumber,
      Instant createdAt,
      UserId createdBy) {
    // Hàm kiểm tra null cho các tham số bắt buộc
    Objects.requireNonNull(userId);
    Objects.requireNonNull(fullName);
    Objects.requireNonNull(email);
    Objects.requireNonNull(passwordHash);
    Objects.requireNonNull(phoneNumber);

    // Trạng thái ban đầu của người dùng là UNVERIFIED, chưa xác thực email và sđt
    Instant now = Instant.now();
    return new User(
        userId,
        fullName,
        email,
        passwordHash,
        phoneNumber,
        UserStatus.UNVERIFIED,
        false,
        false,
        now,
        now,
        createdBy,
        createdBy);
  }

  public static User rehydrate(
      UserId userId,
      String fullName,
      Email email,
      String passwordHash,
      PhoneNumber phoneNumber,
      UserStatus status,
      boolean phoneVerified,
      boolean emailVerified,
      Instant createdAt,
      Instant updatedAt,
      UserId createdBy,
      UserId updatedBy) {
    return new User(
        userId,
        fullName,
        email,
        passwordHash,
        phoneNumber,
        status,
        phoneVerified,
        emailVerified,
        createdAt,
        updatedAt,
        createdBy,
        updatedBy);
  }

  // ============================ BEHAVIOR ============================
  public void changePassword(String newPasswordHash, UserId updatedBy) {
    Objects.requireNonNull(newPasswordHash);
    this.passwordHash = newPasswordHash;
    touch(updatedBy);
  }

  public void updateStatus(UserStatus next, UserId updatedBy) {
    // Sơ đồ trạng thái người dùng
    // UNVERIFIED -> ACTIVE
    // ACTIVE -> DELETED, BLOCKED
    // BLOCKED -> ACTIVE

    // Riêng trạng thái UNVERIED cần kiểm tra thêm điều kiện xác thực email/sđt trước khi chuyển
    // sang ACTIVE
    UserStatus currentStatus = this.status;
    if (currentStatus == UserStatus.UNVERIFIED && next == UserStatus.ACTIVE) {
      if (!this.emailVerified || !this.phoneVerified) {
        throw new AppException(UserErrorCode.CANNOT_ACTIVATE_UNVERIFIED_USER);
      }
    }

    if (!allowedTransitions.containsKey(currentStatus)
        || !allowedTransitions.get(currentStatus).contains(next)) {
      throw new AppException(UserErrorCode.INVALID_USER_STATUS_TRANSITION);
    }

    this.status = next;
    touch(updatedBy);
  }

  public void verifyEmail(UserId updatedBy) {
    this.emailVerified = true;
    touch(updatedBy);
  }

  public void verifyPhone(UserId updatedBy) {
    this.phoneVerified = true;
    touch(updatedBy);
  }

  // ============================ GETTERS ============================
  public UserId getUserId() {
    return userId;
  }

  public String getFullName() {
    return fullName;
  }

  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  public Email getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public boolean isPhoneVerified() {
    return phoneVerified;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public UserStatus getStatus() {
    return status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public UserId getCreatedBy() {
    return createdBy;
  }

  public UserId getUpdatedBy() {
    return updatedBy;
  }

  // ============================ HELPERS ============================
  private void touch(UserId userId) {
    this.updatedAt = Instant.now();
    this.updatedBy = userId;
  }
}
