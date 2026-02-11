package com.uit.se356.core.presentation.rest.exception;

import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.common.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final MessageSource messageSource;

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(
      AppException ex, HttpServletRequest request) {
    String message =
        messageSource.getMessage(
            ex.getErrorCode().getMessageKey(), null, LocaleContextHolder.getLocale());
    ErrorResponse errorResponse =
        new ErrorResponse(
            request.getRequestURI(),
            ex.getErrorCode().getHttpStatus(),
            message.isBlank() ? ex.getMessage() : message,
            null,
            ex.getErrorCode().getCode());
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
  }
}
