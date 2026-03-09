package com.uit.se356.core.domain.vo.authentication.mfa;

public record WebAuthMfaConfig(String credentialId, String publicKey, long signCount)
    implements MfaConfig {}
