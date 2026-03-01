
# API Endpoint Documentation

This document provides a detailed breakdown of all API endpoints, their success responses, and potential error responses.

## General Principles

- **Success Response:** All successful responses are wrapped in an `ApiResponse<T>` object, which has `status: "SUCCESS"` and a `data` field containing the result.
- **Error Response:** All foreseen errors are wrapped in an `ErrorResponse` object, containing a unique `code`, a `message`, and sometimes detailed `errors` for validation failures. The HTTP status code will match the intended error type (e.g., 400, 401, 403, 404, 500).
- **Authentication:** Endpoints under `/api/v1/` generally require a valid `Bearer Token` in the `Authorization` header. Failure to provide one, or providing an invalid/expired one, will result in a `401 Unauthorized` error.
- **Authorization:** Many endpoints require specific permissions. If an authenticated user tries to access an endpoint without the required permission, the API will respond with a `403 Forbidden` error.

---

## OAuth2 Controller

### `GET /oauth2/authorize/{provider}`

- **Description:** Initiates the OAuth2 login flow for a given provider (e.g., "google"). This endpoint does not return JSON; it triggers a browser redirect.
- **Success Response (302 Found):** Redirects the user's browser to the OAuth2 provider's login page.
- **Error Responses:**
    - `500 Internal Server Error`: If the specified `{provider}` is not configured on the server.

---

## Authentication Controller (`/api/v1/auth`)

### `POST /login`

- **Description:** Authenticates a user with their credentials and returns a set of tokens.
- **Request Body:**
  ```json
  {
    "credentialId": "user@example.com",
    "password": "user-password"
  }
  ```
- **Success Response (200 OK):** `ApiResponse<LoginResult>` and sets an HTTP-only `refreshToken` cookie.
- **Error Responses:**
    - `400 Bad Request (AUTH_002)`: `credentialId` is in an invalid format.
    - `400 Bad Request (AUTH_003)`: `password` is missing.
    - `401 Unauthorized (AUTH_001)`: Invalid credentials (user not found or password incorrect).
    - `403 Forbidden (AUTH_011)`: User account is unverified.
    - `403 Forbidden (AUTH_012)`: User account is blocked.

### `POST /rotate-token`

- **Description:** Generates a new pair of access and refresh tokens using a valid refresh token.
- **Request:** `refreshToken` sent either as an HTTP-only cookie (preferred) or in the request body.
- **Success Response (200 OK):** `ApiResponse<LoginResult>` and sets a new HTTP-only `refreshToken` cookie.
- **Error Responses:**
    - `401 Unauthorized (AUTH_016)`: The provided `refreshToken` is missing, invalid, expired, or revoked.

### `POST /send-verification`

- **Description:** Sends a verification code to a user via a specified channel for a specific purpose.
- **Request Body:**
  ```json
  {
    "purpose": "EMAIL_VERIFICATION",
    "channel": "EMAIL",
    "recipient": "user@example.com"
  }
  ```
- **Success Response (200 OK):** `ApiResponse<Void>`
- **Error Responses:**
    - `400 Bad Request (AUTH_007)`: The request body is missing required fields.
    - `400 Bad Request (e.g., AUTH_010)`: Business logic errors from the specific strategy (e.g., trying to verify an already verified email).
    - `500 Internal Server Error (AUTH_005)`: Server is not configured to handle the requested `purpose`.

### `POST /verify-code`

- **Description:** Processes a verification code sent to a user.
- **Request Body:**
  ```json
  {
    "purpose": "PHONE_VERIFICATION",
    "recipient": "0987654321",
    "code": "123456"
  }
  ```
- **Success Response (200 OK):** `ApiResponse<VerificationResult>`
- **Error Responses:**
    - `400 Bad Request (AUTH_007)`: Request body is missing required fields.
    - `400 Bad Request (AUTH_008)`: The `code` is invalid, expired, or does not match the recipient.
    - `500 Internal Server Error (AUTH_005)`: Server is not configured to handle the requested `purpose`.

### `POST /register`

- **Description:** Registers a new user after they have successfully verified their phone number.
- **Request Body:**
  ```json
  {
    "verificationToken": "...",
    "email": "user@example.com",
    "fullName": "Test User",
    "password": "user-password"
  }
  ```
- **Success Response (200 OK):** `ApiResponse<RegisterResult>`
- **Error Responses:**
    - `400 Bad Request (VALIDATION_ERROR)`: Request body is missing required fields.
    - `400 Bad Request (AUTH_008)`: The `verificationToken` is invalid or expired.
    - `400 Bad Request (e.g., AUTH_014)`: Business logic errors like email already in use.
    - `500 Internal Server Error (AUTH_013)`: The default user role is not configured on the server.

### `POST /reset-password`

- **Description:** Resets a user's password using a token from a "forgot password" flow.
- **Request Body:**
  ```json
  {
    "verificationToken": "...",
    "newPassword": "new-strong-password"
  }
  ```
- **Success Response (200 OK):** `ApiResponse<Void>`
- **Error Responses:**
    - `400 Bad Request (VALIDATION_ERROR)`: Request body is missing required fields.
    - `400 Bad Request (AUTH_008)`: The `verificationToken` is invalid or expired.
    - `404 Not Found (USER_005)`: The user associated with the token no longer exists.

### `POST /logout`

- **Description:** Logs out the user by revoking their refresh token.
- **Request:** `refreshToken` sent either as an HTTP-only cookie or in the request body.
- **Success Response (200 OK):** `ApiResponse<Void>` and clears the `refreshToken` cookie.
- **Error Responses:**
    - `401 Unauthorized (AUTH_016)`: The `refreshToken` is missing or invalid.

---

## User Profile Controller (`/api/v1/users`)

### `GET /me`

- **Description:** Retrieves the profile of the currently authenticated user.
- **Permission Required:** Must be authenticated.
- **Success Response (200 OK):** `ApiResponse<UserProfileResult>`
- **Error Responses:**
    - `401 Unauthorized (AUTH_001)`: Not authenticated.
    - `404 Not Found (USER_005)`: User associated with token not found in DB.

### `PUT /me`

- **Description:** Updates the profile of the currently authenticated user.
- **Permission Required:** Must be authenticated.
- **Request Body:**
  ```json
  {
    "fullName": "New Full Name"
  }
  ```
- **Success Response (200 OK):** `ApiResponse<UserProfileResult>`
- **Error Responses:**
    - `400 Bad Request (VALIDATION_ERROR)`: `fullName` is missing.
    - `400 Bad Request (USER_009)`: No change was detected in the user's data.
    - `401 Unauthorized (AUTH_001)`: Not authenticated.
    - `404 Not Found (USER_005)`: User associated with token not found in DB.

---

## Role & Permission Controllers (Admin)

These endpoints are for administrative use and require specific permissions (e.g., `role:create`, `permission:sync`).

### `GET /api/v1/permissions`
- **Permission Required:** `role:read_summary` (or similar admin permission)
- **Description:** Get a paginated list of all available permissions.
- **Success:** `ApiResponse<PageResponse<PermissionSummaryProjection>>`
- **Errors:** `401`, `403`, `400` (invalid query params).

### `GET /api/v1/roles`
- **Permission Required:** `role:read_summary`
- **Description:** Get a paginated list of all roles.
- **Success:** `ApiResponse<PageResponse<RoleSummaryProjection>>`
- **Errors:** `401`, `403`, `400` (invalid query params).

### `POST /api/v1/roles`
- **Permission Required:** `role:create`
- **Description:** Create a new role.
- **Success:** `ApiResponse<RoleResult>`
- **Errors:** `401`, `403`, `400` (validation error, e.g., duplicate name).

### `PUT /api/v1/roles/{id}`
- **Permission Required:** `role:update`
- **Description:** Update an existing role.
- **Success:** `ApiResponse<RoleResult>`
- **Errors:** `401`, `403`, `404` (role not found), `400` (validation error).

### `DELETE /api/v1/roles/{id}`
- **Permission Required:** `role:delete`
- **Description:** Delete a role.
- **Success:** `ApiResponse<Void>`
- **Errors:** `401`, `403`, `404` (role not found), `400` (role in use or system role).

### `POST /api/v1/roles/{id}/permissions`
- **Permission Required:** `role:assign_permission`
- **Description:** Assign a set of permissions to a role.
- **Success:** `ApiResponse<Void>`
- **Errors:** `401`, `403`, `404` (role not found), `400` (permission not found, modifying system role).

---

## Internal Controller (Admin/Dev)

**Warning:** These endpoints are for internal administrative or debugging purposes and can be dangerous.

### `POST /api/v1/internal/permissions/sync`
- **Permission Required:** `permission:sync`
- **Description:** **DESTRUCTIVE**. Deletes all permissions and rebuilds them by scanning code annotations.
- **Success:** `ApiResponse<Void>`
- **Errors:** `401`, `403`.

### `GET /api/v1/internal/debug-otp`
- **Permission Required:** `debug:otp:read`
- **Description:** Retrieves the current OTP for a phone number from the cache for debugging.
- **Success:** `ApiResponse<DebugOtpResult>`
- **Errors:** `401`, `403`, `400` (phone number missing).
