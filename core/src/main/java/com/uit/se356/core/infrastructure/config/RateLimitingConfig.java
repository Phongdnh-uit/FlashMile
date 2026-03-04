package com.uit.se356.core.infrastructure.config;

import com.uit.se356.core.infrastructure.security.filter.RateLimitingFilter;
import io.github.bucket4j.Bandwidth;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
@RequiredArgsConstructor
public class RateLimitingConfig {

  private final ObjectMapper objectMapper;
  private final MessageSource messageSource;

  @Bean
  FilterRegistrationBean<RateLimitingFilter> authRateLimitingFilter() {
    FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();

    // Rate limit cho đăng nhập: 5 yêu cầu mỗi phút
    Bandwidth limit =
        Bandwidth.builder().capacity(5).refillIntervally(5, Duration.ofMinutes(1)).build();
    registrationBean.setFilter(new RateLimitingFilter(limit, objectMapper, messageSource));
    registrationBean.addUrlPatterns("/api/v1/auth/*");
    registrationBean.setOrder(1);

    return registrationBean;
  }

  @Bean
  FilterRegistrationBean<RateLimitingFilter> uploadRateLimitingFilter() {
    FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();

    // Rate limit cho upload: 10 yêu cầu mỗi phút
    Bandwidth limit =
        Bandwidth.builder().capacity(10).refillIntervally(10, Duration.ofMinutes(1)).build();

    registrationBean.setFilter(new RateLimitingFilter(limit, objectMapper, messageSource));
    registrationBean.addUrlPatterns("/api/v1/upload/*");
    registrationBean.setOrder(1);

    return registrationBean;
  }
}
