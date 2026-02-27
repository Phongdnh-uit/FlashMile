package com.uit.se356.core.presentation.exception;

import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

  @ApiResponse(
      responseCode = "400",
      description = "Bad Request",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(
      AppException ex, HttpServletRequest request) {
    String message =
        messageSource.getMessage(
            ex.getErrorCode().getMessageKey(), null, LocaleContextHolder.getLocale());
    // Chuyển detail trong exception nếu là dạng map
    List<FieldError> fieldErrors = new ArrayList<>();
    if (ex.getDetails() instanceof Map errors) {
      errors.forEach(
          (field, error) -> fieldErrors.add(new FieldError(field.toString(), error.toString())));
    }

    ErrorResponse errorResponse =
        new ErrorResponse(
            request.getRequestURI(),
            ex.getErrorCode().getHttpStatus(),
            message.isBlank() ? ex.getMessage() : message,
            fieldErrors,
            ex.getErrorCode().getCode());
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
  }
}
