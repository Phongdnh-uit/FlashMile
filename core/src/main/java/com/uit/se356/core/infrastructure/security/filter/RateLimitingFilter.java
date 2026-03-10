package com.uit.se356.core.infrastructure.security.filter;

import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

  private final Bandwidth limit;
  private final ObjectMapper objectMapper;
  private final MessageSource messageSource;
  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String clientId = extractClientIp(request);
    Bucket bucket = this.buckets.computeIfAbsent(clientId, k -> createNewBucket());

    if (bucket.tryConsume(1)) {
      filterChain.doFilter(request, response);
    } else {
      long waitForRefill = bucket.getAvailableTokens();
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
      response.setHeader("Retry-After", String.valueOf(waitForRefill));
      response.setContentType("application/json");
      objectMapper.writeValue(response.getWriter(), errorResponse);
    }
  }

  private Bucket createNewBucket() {
    return Bucket.builder().addLimit(limit).build();
  }

  private String extractClientIp(HttpServletRequest request) {
    String xf = request.getHeader("X-Forwarded-For");
    return (xf != null && !xf.isBlank()) ? xf.split(",")[0].trim() : request.getRemoteAddr();
  }
}
