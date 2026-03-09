package com.uit.se356.core.domain.vo.authentication.mfa;

public record TotpMfaConfig(String secretKey) implements MfaConfig {}
