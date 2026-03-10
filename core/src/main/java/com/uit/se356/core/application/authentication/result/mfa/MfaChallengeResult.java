package com.uit.se356.core.application.authentication.result.mfa;

import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import java.util.Objects;

public record MfaChallengeResult(
    MfaMethod method,
    String challengeId,
    String status, // "SENT" (Email), "REQUIRED" (TOTP), "CHALLENGE_GENERATED" (WebAuthn)
    String publicOptionsJson, // Dùng cho WebAuthn (PublicKeyCredentialRequestOptions)
    String maskedTarget // Ví dụ: "u***@gmail.com" để hiển thị cho người dùng
    ) {
  public MfaChallengeResult {
    // Tầng dưới bắt buộc phải đảm bảo có challengeId
    Objects.requireNonNull(challengeId, "challengeId must not be null");
  }

  public static MfaChallengeResult email(String target) {
    return new MfaChallengeResult(MfaMethod.EMAIL, null, "SENT", null, target);
  }

  public static MfaChallengeResult webAuthn(String optionsJson) {
    return new MfaChallengeResult(MfaMethod.WEBAUTHN, null, "REQUIRED", optionsJson, null);
  }

  public static MfaChallengeResult totp() {
    return new MfaChallengeResult(MfaMethod.TOTP, null, "REQUIRED", null, null);
  }
}
