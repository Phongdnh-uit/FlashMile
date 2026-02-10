package com.uit.se356.core.presentation.web;

import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.common.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(
      AppException ex, HttpServletRequest request) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            request.getRequestURI(),
            ex.getErrorCode().getHttpStatus(),
            ex.getMessage(),
            null,
            ex.getErrorCode().getCode());
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
  }
}
