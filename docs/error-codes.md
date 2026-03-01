# Error Codes

This document provides a list of all error codes used in the application.

## Common Errors

| Code | Message Key | HTTP Status |
| :--- | :--- | :--- |
| COMMON-0001 | error.common.email.sending_failed | 500 |
| COMMON-0000 | error.common.invalid_id_format | 400 |
| COMMON-0002 | error.common.validation_error | 400 |
| COMMON-0003 | error.common.internal_error | 500 |
| COMMON-0004 | error.common.invalid_sort_order | 400 |

## Authentication Errors

| Code | Message Key | HTTP Status |
| :--- | :--- | :--- |
| AUTH_001 | error.auth.invalid_credentials | 401 |
| AUTH_002 | error.auth.credential_id_invalid | 400 |
| AUTH_003 | error.auth.password_invalid | 400 |
| AUTH_004 | error.auth.token_generation_failed | 500 |
| AUTH_005 | error.auth.uncategorized | 500 |
| AUTH_006 | error.auth.phone_already_registered | 400 |
| AUTH_007 | error.auth.invalid_verification_code_request | 400 |
| AUTH_008 | error.auth.invalid_verification_code | 400 |
| AUTH_009 | error.auth.invalid_register_command | 400 |
| AUTH_010 | error.auth.email_already_verified | 400 |
| AUTH_011 | error.auth.user_unverified | 403 |
| AUTH_012 | error.auth.user_blocked | 403 |
| AUTH_013 | error.auth.role_not_found | 500 |
| AUTH_014 | error.auth.email_already_used | 400 |
| AUTH_015 | error.auth.oauth2_authorization_request_failed | 401 |
| AUTH_016 | error.auth.invalid_token | 401 |
| AUTH_017 | error.auth.access_denied | 403 |
| AUTH_018 | error.auth.too_many_requests | 429 |
| AUTH_019 | error.auth.role_cannot_be_deleted | 400 |
| AUTH_020 | error.auth.token_expired | 401 |
| AUTH_021 | error.auth.authentication_required | 401 |
| AUTH_022 | error.auth.system_role_modification | 400 |
| AUTH_023 | error.auth.permission_not_found | 400 |

## Verification Errors

| Code | Message Key | HTTP Status |
| :--- | :--- | :--- |
| VERIFICATION_001 | error.verification.invalid_id | 400 |
| VERIFICATION_002 | error.verification.invalid_expires_at | 400 |

## User Errors

| Code | Message Key | HTTP Status |
| :--- | :--- | :--- |
| USER_001 | error.user.id.invalid | 400 |
| USER_002 | error.user.email.format | 400 |
| USER_003 | error.user.phone.format | 400 |
| USER_004 | error.user.fullname.invalid | 400 |
| USER_005 | error.user.not_found | 404 |
| USER_006 | error.user.exists | 409 |
| USER_007 | error.user.status.transition.invalid | 400 |
| USER_008 | error.user.cannot_activate_unverified | 400 |
| NO_CHANGE_DETECTED | error.user.no_change_detected | 400 |
