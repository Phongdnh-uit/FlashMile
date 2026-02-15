package com.uit.se356.common.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OtpGenerator {
  private static String PATTERN = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final SecureRandom RANDOM = createSecureRandom();

  public static String generateOtp(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(PATTERN.charAt(RANDOM.nextInt(PATTERN.length())));
    }
    return sb.toString();
  }

  private static SecureRandom createSecureRandom() {
    try {
      return SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      return new SecureRandom();
    }
  }
}
