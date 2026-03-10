package com.uit.se356.core.application.authentication.port.out;

public interface EncryptService {
  String encrypt(String plainText);

  String decrypt(String cipherText);
}
