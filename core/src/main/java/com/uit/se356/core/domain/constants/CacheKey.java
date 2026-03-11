package com.uit.se356.core.domain.constants;

public interface CacheKey {
  String PHONE_VERIFICATION_CODE_PREFIX = "PHONE_VERIFICATION";
  String PHONE_VERIFIED_PREFIX = "PHONE_VERIFIED:TOKEN";
  String PERMISSION_LIST = "PERMISSION_LIST";
  String MFA_PRECHALLENGE_PREFIX = "MFA_CHALLENGE:{verificationToken}";
  String MFA_CHALLENGE_PREFIX = "MFA_CHALLENGE:{challengeId}";
}
