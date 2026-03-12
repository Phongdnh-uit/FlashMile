package com.uit.se356.core.application.authentication.result.mfa;

import java.util.List;

public record CompleteSetupMfaResult(List<String> backupCodes) {}
