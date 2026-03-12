package com.uit.se356.core.presentation.request.mfa;

import lombok.Data;

@Data
public class VerifyWebAuthnRequest {
    private byte[] credentialId;
    private byte[] authenticatorData;
    private byte[] clientDataJSON;
    private byte[] signature;
}
