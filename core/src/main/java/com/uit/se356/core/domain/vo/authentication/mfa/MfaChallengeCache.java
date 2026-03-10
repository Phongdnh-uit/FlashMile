package com.uit.se356.core.domain.vo.authentication.mfa;

import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Objects;

public record MfaChallengeCache(UserId userId, MfaMethod method) {
  public MfaChallengeCache {
    // Không dùng FieldError vì đây là vo bắt buộc dev phải truyền vào
    Objects.requireNonNull(userId, "userId must not be null");
    Objects.requireNonNull(method, "method must not be null");
  }
}
