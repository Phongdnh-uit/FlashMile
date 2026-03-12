package com.uit.se356.core.infrastructure.provider.mfa;

import com.uit.se356.core.application.authentication.port.out.MfaProvider;
import com.uit.se356.core.domain.vo.authentication.mfa.WebAuthMfaConfig;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.data.*;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.server.ServerProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebAuthnProvider implements MfaProvider {

    private final WebAuthnManager webAuthnManager;

    @Override
    public boolean supports(String method) {
        return "WEBAUTHN".equalsIgnoreCase(method);
    }

    // ... other methods to be implemented
    @Override
    public MfaSetupResult initiateMfaSetup(UserId userId) {
        RelyingParty rp = new RelyingParty("your-relying-party-id", "Your App Name", new Origin("http://localhost:3000"));
        Challenge challenge = new DefaultChallenge();
        PublicKeyCredentialUserEntity userEntity = new PublicKeyCredentialUserEntity(userId.getValue().toString().getBytes(), userId.getValue().toString(), "display-name");

        PublicKeyCredentialParameters... pubKeyCredParams = // ... define your parameters

        PublicKeyCredentialCreationOptions creationOptions = new PublicKeyCredentialCreationOptions(
            rp,
            userEntity,
            challenge,
            pubKeyCredParams
            // ... other options
        );

        // This needs to be stored temporarily to be verified in the completeMfaSetup step
        // A cache (like Redis) or the session can be used for this.

        return new MfaSetupResult(null, creationOptions.toPropertyMap());
    }

    public WebAuthMfaConfig completeMfaSetup(byte[] attestationObject, byte[] clientDataJSON, UserId userId) {
        // Retrieve the server property from the temporary store (cache/session)

        RegistrationData registrationData = webAuthnManager.parse(new RegistrationRequest(attestationObject, clientDataJSON));
        RegistrationParameters registrationParameters = new RegistrationParameters(
            // server property from temporary store
            // ...
        );
        RegistrationResult registrationResult = webAuthnManager.validate(registrationData, registrationParameters);

        // Map from webauthn4j's objects to our domain object
        WebAuthMfaConfig config = new WebAuthMfaConfig();
        config.setCredentialId(registrationResult.getAttestedCredentialData().getCredentialId());
        config.setPublicKeyCose(registrationResult.getAttestedCredentialData().getCOSEKey().getValue());
        config.setSignCount(registrationResult.getAttestationObject().getAuthenticatorData().getSignCount());
        return config;
    }

    public Map<String, String> challenge(Mfa mfa) {
         WebAuthMfaConfig config = (WebAuthMfaConfig) mfa.getConfig();
         Challenge challenge = new DefaultChallenge();

         PublicKeyCredentialRequestOptions requestOptions = new PublicKeyCredentialRequestOptions(
            challenge,
            60000L,
            "your-relying-party-id",
            List.of(new PublicKeyCredentialDescriptor(PublicKeyCredentialType.PUBLIC_KEY, config.getCredentialId(), null)),
            null
         );

        // Store the challenge temporarily for verification

        return requestOptions.toPropertyMap();
    }

    public void verify(Mfa mfa, byte[] credentialId, byte[] authenticatorData, byte[] clientDataJSON, byte[] signature) {
        WebAuthMfaConfig config = (WebAuthMfaConfig) mfa.getConfig();

        AuthenticationData authenticationData = webAuthnManager.parse(new AuthenticationRequest(credentialId, authenticatorData, clientDataJSON, signature));
        AuthenticationParameters authenticationParameters = new AuthenticationParameters(
            // server property with challenge from temp store
            // ...
        );

        AuthenticationResult authenticationResult = webAuthnManager.validate(authenticationData, authenticationParameters);

        // Update the sign count in the MfaConfig and persist it
        config.setSignCount(authenticationResult.getAuthenticatorData().getSignCount());
    }
}
