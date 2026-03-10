package com.uit.se356.core.application.authentication.result;

import com.uit.se356.core.application.authentication.projections.MfaMethodProjection;
import java.util.List;

public record LoginResult(
    String accessToken,
    String refreshToken,
    long expiresIn,
    String tokenType,
    boolean mfaRequired,
    List<MfaMethodProjection> mfaMethods,
    String verificationToken) {

  // Fallback để không phải sửa lại toàn bộ code
  public LoginResult(String accessToken, String refreshToken, long expiresIn, String tokenType) {
    this(accessToken, refreshToken, expiresIn, tokenType, false, null, null);
  }

  public static LoginResult mfaRequired(
      List<MfaMethodProjection> mfaMethods, String verificationToken) {
    return new LoginResult(null, null, 0, null, true, mfaMethods, verificationToken);
  }
}
