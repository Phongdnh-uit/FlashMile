package com.uit.se356.core.infrastructure.security.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class AuthRateLimitFilter extends OncePerRequestFilter {
  // Sử dụng Caffeine để lưu trữ tạm, thay thế ConcurrentMap , có thể nâng lên dùng Redis nếu phân
  // tán
  private final Cache<String, Bucket> buckets =
      Caffeine.newBuilder().expireAfterAccess(Duration.ofMinutes(10)).maximumSize(10_000).build();
  private final ObjectMapper objectMapper;

  private final MessageSource messageSource;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Chỉ lấy các endpoint chứa auth
    if (request.getRequestURI().startsWith("/api/v1/auth")) {
      String clientId = extractClientIp(request);
      Bucket bucket = getBucket(clientId);
      if (bucket.tryConsume(1)) {
        filterChain.doFilter(request, response);
      } else {
        ErrorResponse errorResponse =
            new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                messageSource.getMessage(
                    AuthErrorCode.TOO_MANY_REQUESTS.getMessageKey(),
                    null,
                    LocaleContextHolder.getLocale()),
                null,
                AuthErrorCode.TOO_MANY_REQUESTS.getCode());
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), errorResponse);
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private Bucket getBucket(String clientId) {
    return buckets.get(
        clientId,
        k ->
            Bucket.builder()
                .addLimit(
                    Bandwidth.builder()
                        .capacity(5)
                        .refillIntervally(5, Duration.ofMinutes(1))
                        .build())
                .build());
  }

  private String extractClientIp(HttpServletRequest request) {
    String xf = request.getHeader("X-Forwarded-For");
    return (xf != null && !xf.isBlank()) ? xf.split(",")[0].trim() : request.getRemoteAddr();
  }
}
