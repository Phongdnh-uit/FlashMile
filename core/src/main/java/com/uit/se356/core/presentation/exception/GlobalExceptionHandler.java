package com.uit.se356.core.presentation.exception;

import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
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
    log.error("AppException: {}", errorResponse);
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    String message =
        messageSource.getMessage(
            CommonErrorCode.VALIDATION_ERROR.getMessageKey(),
            null,
            LocaleContextHolder.getLocale());
    List<FieldError> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error ->
                    new FieldError(
                        error.getField(),
                        messageSource.getMessage(error, LocaleContextHolder.getLocale())))
            .collect(Collectors.toList());
    ErrorResponse errorResponse =
        new ErrorResponse(
            request.getRequestURI(),
            CommonErrorCode.VALIDATION_ERROR.getHttpStatus(),
            message,
            fieldErrors,
            CommonErrorCode.VALIDATION_ERROR.getCode());
    log.error("Validation Exception: {}", errorResponse);
    return ResponseEntity.status(CommonErrorCode.VALIDATION_ERROR.getHttpStatus())
        .body(errorResponse);
  }

  @ExceptionHandler({Exception.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, HttpServletRequest request) {
    String message =
        messageSource.getMessage(
            CommonErrorCode.INTERNAL_ERROR.getMessageKey(), null, LocaleContextHolder.getLocale());
    ErrorResponse errorResponse =
        new ErrorResponse(
            request.getRequestURI(),
            CommonErrorCode.INTERNAL_ERROR.getHttpStatus(),
            message,
            null,
            CommonErrorCode.INTERNAL_ERROR.getCode());
    log.error("Unexpected Exception: ", ex);
    return ResponseEntity.status(CommonErrorCode.INTERNAL_ERROR.getHttpStatus())
        .body(errorResponse);
  }
}
