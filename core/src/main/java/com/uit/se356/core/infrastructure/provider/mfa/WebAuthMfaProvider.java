package com.uit.se356.core.infrastructure.provider.mfa;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.port.out.MfaProvider;
import com.uit.se356.core.application.authentication.port.out.MfaRepository;
import com.uit.se356.core.application.authentication.result.mfa.MfaChallengeResult;
import com.uit.se356.core.application.authentication.result.mfa.MfaSetupResult;
import com.uit.se356.core.application.authentication.result.mfa.MfaVerifyResult;
import com.uit.se356.core.domain.entities.authentication.Mfa;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.mfa.MfaConfig;
import com.uit.se356.core.domain.vo.authentication.mfa.WebAuthMfaConfig;
import com.uit.se356.core.infrastructure.config.AppProperties;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.AttestationConveyancePreference;
import com.webauthn4j.data.AuthenticationData;
import com.webauthn4j.data.AuthenticationParameters;
import com.webauthn4j.data.AuthenticatorAttachment;
import com.webauthn4j.data.AuthenticatorSelectionCriteria;
import com.webauthn4j.data.PublicKeyCredentialCreationOptions;
import com.webauthn4j.data.PublicKeyCredentialDescriptor;
import com.webauthn4j.data.PublicKeyCredentialHints;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialRequestOptions;
import com.webauthn4j.data.PublicKeyCredentialRpEntity;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.data.PublicKeyCredentialUserEntity;
import com.webauthn4j.data.RegistrationData;
import com.webauthn4j.data.RegistrationParameters;
import com.webauthn4j.data.UserVerificationRequirement;
import com.webauthn4j.data.attestation.authenticator.AAGUID;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.authenticator.COSEKey;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.verifier.exception.VerificationException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebAuthMfaProvider implements MfaProvider {

  private final ObjectMapper objectMapper;
  private final MfaRepository mfaRepository;
  private final AppProperties appProperties;
  private final WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
  private final ObjectConverter objectConverter = new ObjectConverter();

  @Override
  public boolean supports(MfaMethod method) {
    return method == MfaMethod.WEBAUTHN;
  }

  @Override
  public MfaSetupResult<WebAuthMfaConfig> initiateMfaSetup(UserId userId) {
    PublicKeyCredentialRpEntity rp = new PublicKeyCredentialRpEntity("FlashMile");
    byte[] challengeValue = generateRandomChallenge();
    Challenge challenge = new DefaultChallenge(challengeValue);
    PublicKeyCredentialUserEntity user =
        new PublicKeyCredentialUserEntity(
            userId.value().toString().getBytes(StandardCharsets.UTF_8),
            userId.value().toString(),
            userId.value().toString());

    List<PublicKeyCredentialParameters> pubKeyCredParams =
        List.of(
            new PublicKeyCredentialParameters(
                PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256),
            new PublicKeyCredentialParameters(
                PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256));

    AuthenticatorSelectionCriteria authenticatorSelection =
        new AuthenticatorSelectionCriteria(
            AuthenticatorAttachment.PLATFORM, false, UserVerificationRequirement.PREFERRED);

    List<PublicKeyCredentialHints> hints = List.of(PublicKeyCredentialHints.CLIENT_DEVICE);

    PublicKeyCredentialCreationOptions options =
        new PublicKeyCredentialCreationOptions(
            rp,
            user,
            challenge,
            pubKeyCredParams,
            60000L, // Timeout in milliseconds (1 minute)
            Collections.emptyList(),
            authenticatorSelection,
            hints,
            AttestationConveyancePreference.NONE,
            null,
            null);
    // Cần lưu lại challenge này vào DB để hàm verify() có thể lấy lên so sánh sau này

    WebAuthMfaConfig config =
        new WebAuthMfaConfig(
            challengeValue, // challenge
            new byte[0], // credentialId chưa có
            new byte[0], // publicKey chưa có
            0, // signCount chưa có
            List.of() // Transport chưa có
            );
    Map<String, String> metadata = new HashMap<>();
    metadata.put("publicKeyCredentialCreationOptions", objectMapper.writeValueAsString(options));

    return new MfaSetupResult<>(config, metadata);
  }

  @Override
  public MfaChallengeResult initiateMfaChallenge(UserId userId, MfaMethod method) {
    // 1. Lấy danh sách các khóa đã đăng ký từ DB (Chỉ lấy các bản ghi ACTIVE)
    Optional<Mfa> mfaOpt = mfaRepository.findByUserIdAndMethod(userId, method);

    if (mfaOpt.isEmpty() || !mfaOpt.get().isVerified()) {
      throw new AppException(AuthErrorCode.MFA_METHOD_NOT_FOUND);
    }

    // 2. Tạo Challenge mới
    byte[] challengeValue = generateRandomChallenge(); // 32 bytes ngẫu nhiên
    Challenge challenge = new DefaultChallenge(challengeValue);

    WebAuthMfaConfig config = (WebAuthMfaConfig) mfaOpt.get().getConfig();
    if (config.credentialId() == null || config.credentialId().length == 0) {
      log.error("User {} has no registered credentialId for WebAuthn MFA", userId.value());
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
    // 3. Chuyển đổi danh sách config từ DB thành danh sách "cho phép" (AllowCredentials)
    PublicKeyCredentialDescriptor allowCredential =
        new PublicKeyCredentialDescriptor(
            PublicKeyCredentialType.PUBLIC_KEY, config.credentialId(), null);

    // 4. Tạo Options để gửi về Frontend (PublicKeyCredentialRequestOptions)
    // rpId phải khớp với rpId lúc đăng ký (ví dụ: "flashmile.com")
    PublicKeyCredentialRequestOptions options =
        new PublicKeyCredentialRequestOptions(
            challenge,
            60000L, // Timeout 1 phút
            "FlashMile", // rpId
            List.of(allowCredential), // allowCredentials
            UserVerificationRequirement.REQUIRED, // Ưu tiên quét vân tay/FaceID
            null);

    // 5. CẬP NHẬT DB: Lưu challenge mới này vào bản ghi của User
    // Bạn cần lưu challenge này để hàm verify() có thể lấy lên so sánh
    WebAuthMfaConfig updatedConfig =
        new WebAuthMfaConfig(
            challengeValue,
            config.credentialId(),
            config.publicKeyCos(),
            config.signCount(),
            config.transports());
    mfaOpt.get().updateConfig(updatedConfig);
    mfaRepository.update(mfaOpt.get());

    return MfaChallengeResult.webAuthn(objectMapper.writeValueAsString(options));
  }

  @Override
  public MfaVerifyResult verify(MfaConfig config, String credentialJson) {
    WebAuthMfaConfig webAuthConfig = (WebAuthMfaConfig) config;
    Challenge storedChallenge = new DefaultChallenge(webAuthConfig.challenge());

    // ServerProperty theo đúng đặc tả RP ID và Origin của FlashMile
    ServerProperty serverProperty =
        ServerProperty.builder()
            .rpId("FlashMile")
            .challenge(storedChallenge)
            .origin(new Origin(appProperties.getFrontend().getBaseUrl()))
            .build();

    try {
      // Kiểm tra luồng dựa trên sự tồn tại của Public Key trong DB
      if (webAuthConfig.publicKeyCos() == null || webAuthConfig.publicKeyCos().length == 0) {
        // LUỒNG SETUP: Parse sang RegistrationRequest
        RegistrationData registrationRequest =
            webAuthnManager.parseRegistrationResponseJSON(credentialJson);
        return verifyRegistration(registrationRequest, serverProperty, webAuthConfig);
        // Trả về config mới
      } else {
        // LUỒNG LOGIN: Parse sang AuthenticationData
        AuthenticationData authData =
            webAuthnManager.parseAuthenticationResponseJSON(credentialJson);
        return verifyAuthentication(authData, serverProperty, webAuthConfig);
      }
    } catch (Exception e) {
      log.error("WebAuthn verification failed", e);
      return new MfaVerifyResult(false, null);
    }
  }

  // ============================ HELPERS ============================

  private byte[] generateRandomChallenge() {
    byte[] challenge = new byte[32]; // 32 bytes = 256 bits
    new SecureRandom().nextBytes(challenge);
    return challenge;
  }

  private MfaVerifyResult verifyRegistration(
      RegistrationData registrationData, ServerProperty serverProperty, WebAuthMfaConfig config) {
    try {
      // Sử dụng RegistrationParameters mới
      RegistrationParameters registrationParameters =
          new RegistrationParameters(serverProperty, null, true, true);

      // Verify bằng manager
      RegistrationData validatedData =
          webAuthnManager.verify(registrationData, registrationParameters);

      // Trích xuất thông tin để cập nhật vào bản ghi config (sau đó bạn cần save bản ghi này vào
      // DB)
      var authData = validatedData.getAttestationObject().getAuthenticatorData();
      var credentialData = authData.getAttestedCredentialData();

      // Cập nhật lại Object để lớp gọi hàm này có thể lấy data lưu vào DB
      // Giả sử bạn cập nhật vào chính đối tượng config

      // config.setCredentialId(credentialData.getCredentialId());
      // config.setPublicKeyCos(credentialData.getCOSEKey().getEncoded());
      WebAuthMfaConfig updatedConfig =
          new WebAuthMfaConfig(
              config.challenge(),
              credentialData.getCredentialId(),
              credentialData.getCOSEKey().getPublicKey().getEncoded(),
              0, // signCount ban đầu là 0
              List.of() // Transport chưa có
              );

      return new MfaVerifyResult(true, updatedConfig);
    } catch (VerificationException e) {
      return new MfaVerifyResult(false, null);
    }
  }

  private MfaVerifyResult verifyAuthentication(
      AuthenticationData authenticationData,
      ServerProperty serverProperty,
      WebAuthMfaConfig webAuthConfig) {
    try {
      // 1. Tái tạo Public Key từ DB thành đối tượng COSEKey
      COSEKey publicKey =
          objectConverter.getCborMapper().readValue(webAuthConfig.publicKeyCos(), COSEKey.class);

      // 2. Tạo CredentialRecord thay cho Authenticator
      AttestedCredentialData attestedCredentialData =
          new AttestedCredentialData(
              AAGUID.ZERO, // AAGUID có thể để ZERO nếu không có thông tin cụ thể về thiết bị
              webAuthConfig.credentialId(),
              publicKey);
      CredentialRecord credentialRecord =
          new CredentialRecordImpl(
              null, // attestationStatement
              true, // uvInitialized: true vì mình dùng TouchID/FaceID
              false, // backupEligible: mặc định false nếu không dùng Passkey đồng bộ
              false, // backupState: mặc định false
              webAuthConfig.signCount(), // counter (đây chính là signCount)
              attestedCredentialData, // attestedCredentialData (Must not be null)
              null, // authenticatorExtensions
              null, // clientData
              null, // clientExtensions
              Collections.emptySet() // transports (nên dùng emptySet thay vì null để an toàn)
              );

      // 3. Tạo AuthenticationParameters (Vẫn dùng Interface này nhưng truyền CredentialRecord vào)
      // Thư viện thường chấp nhận CredentialRecord ở vị trí của Authenticator do tính đa hình
      AuthenticationParameters authParams =
          new AuthenticationParameters(
              serverProperty,
              credentialRecord, // Truyền record mới vào đây
              null,
              true);

      // 4. Thực hiện verify
      webAuthnManager.verify(authenticationData, authParams);

      // 5. Kiểm tra sign count chống cloning
      long newSignCount = authenticationData.getAuthenticatorData().getSignCount();
      if (newSignCount > 0 && newSignCount <= webAuthConfig.signCount()) {
        return new MfaVerifyResult(false, null);
      }
      WebAuthMfaConfig updatedConfig =
          new WebAuthMfaConfig(
              webAuthConfig.challenge(),
              webAuthConfig.credentialId(),
              webAuthConfig.publicKeyCos(),
              newSignCount, // Cập nhật signCount mới
              webAuthConfig.transports());
      return new MfaVerifyResult(true, updatedConfig);
    } catch (VerificationException e) {
      log.error("Authentication verification failed", e);
      return new MfaVerifyResult(false, null);
    }
  }
}
