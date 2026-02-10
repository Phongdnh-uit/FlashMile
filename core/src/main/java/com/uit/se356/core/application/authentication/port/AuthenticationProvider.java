package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.application.authentication.result.LoginResult;

public interface AuthenticationProvider {
  LoginResult authenticate(String credentialId, String password);
}
