package com.uit.se356.core.presentation.request.mfa;

import lombok.Data;

@Data
public class CompleteWebAuthnSetupRequest {
    private byte[] attestationObject;
    private byte[] clientDataJSON;
    // other fields from PublicKeyCredential
}
