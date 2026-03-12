package com.uit.se356.core.domain.vo.authentication.mfa;

import lombok.Data;

@Data
public class WebAuthMfaConfig implements MfaConfig {
    private byte[] credentialId;
    private byte[] publicKeyCose;
    private long signCount;
}
