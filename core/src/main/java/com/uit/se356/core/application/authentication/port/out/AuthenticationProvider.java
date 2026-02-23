package com.uit.se356.core.application.authentication.port.out;

import com.uit.se356.core.application.authentication.result.LoginResult;

public interface AuthenticationProvider {
  LoginResult authenticate(String credentialId, String password);
}
