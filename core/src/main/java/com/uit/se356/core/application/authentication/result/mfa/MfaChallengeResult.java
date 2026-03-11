package com.uit.se356.core.application.authentication.result.mfa;

import com.uit.se356.core.domain.vo.authentication.MfaMethod;

public record MfaChallengeResult(
    MfaMethod method,
    String challengeId,
    String status, // "SENT" (Email), "REQUIRED" (TOTP), "CHALLENGE_GENERATED" (WebAuthn)
    String publicOptionsJson, // Dùng cho WebAuthn (PublicKeyCredentialRequestOptions)
    String maskedTarget // Ví dụ: "u***@gmail.com" để hiển thị cho người dùng
    ) {
  public static MfaChallengeResult email(String target) {
    return new MfaChallengeResult(MfaMethod.EMAIL, null, "SENT", null, target);
  }

  public static MfaChallengeResult webAuthn(String optionsJson) {
    return new MfaChallengeResult(MfaMethod.WEBAUTHN, null, "REQUIRED", optionsJson, null);
  }

  public static MfaChallengeResult totp() {
    return new MfaChallengeResult(MfaMethod.TOTP, null, "REQUIRED", null, null);
  }

  public MfaChallengeResult withChallengeId(String challengeId) {
    return new MfaChallengeResult(
        this.method, challengeId, this.status, this.publicOptionsJson, this.maskedTarget);
  }
}
