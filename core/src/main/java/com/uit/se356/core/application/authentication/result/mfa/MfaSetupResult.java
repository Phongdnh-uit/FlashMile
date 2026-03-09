package com.uit.se356.core.application.authentication.result.mfa;

import com.uit.se356.core.domain.vo.authentication.mfa.MfaConfig;
import java.util.Map;

public record MfaSetupResult<T extends MfaConfig>(T config, Map<String, String> metadata) {}
