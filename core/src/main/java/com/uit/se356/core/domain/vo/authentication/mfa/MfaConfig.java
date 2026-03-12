package com.uit.se356.core.domain.vo.authentication.mfa;

public sealed interface MfaConfig permits TotpMfaConfig, EmailMfaConfig, WebAuthMfaConfig {}
