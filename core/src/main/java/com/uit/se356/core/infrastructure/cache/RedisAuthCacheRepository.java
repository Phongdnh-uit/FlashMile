package com.uit.se356.core.infrastructure.cache;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.port.out.AuthCacheRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisAuthCacheRepository implements AuthCacheRepository {

  private final ObjectMapper objectMapper;
  private final StringRedisTemplate redisTemplate;

  @Override
  public void set(String key, String value, Duration ttl) {
    redisTemplate.opsForValue().set(key, value, ttl);
  }

  @Override
  public void set(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(key));
  }

  @Override
  public void delete(String key) {
    redisTemplate.delete(key);
  }

  @Override
  public boolean exists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  @Override
  public void expire(String key, Duration ttl) {
    redisTemplate.expire(key, ttl);
  }

  @Override
  public void setSet(String key, Set<String> values, Duration ttl) {
    redisTemplate.opsForSet().add(key, values.toArray(new String[0]));
    redisTemplate.expire(key, ttl);
  }

  @Override
  public Optional<Set<String>> getSet(String key) {
    Set<String> members = redisTemplate.opsForSet().members(key);
    return Optional.ofNullable(members);
  }

  @Override
  public <T> void setObject(String key, T value) {
    try {
      String json = objectMapper.writeValueAsString(value);
      redisTemplate.opsForValue().set(key, json);
    } catch (Exception e) {
      log.error("Failed to serialize object to JSON", e);
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }

  @Override
  public <T> void setObject(String key, T value, Duration ttl) {
    try {
      String json = objectMapper.writeValueAsString(value);
      redisTemplate.opsForValue().set(key, json, ttl);
    } catch (Exception e) {
      log.error("Failed to serialize object to JSON", e);
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }

  @Override
  public <T> Optional<T> getObject(String key, Class<T> clazz) {
    try {
      String json = redisTemplate.opsForValue().get(key);
      if (json == null) {
        return Optional.empty();
      }
      T value = objectMapper.readValue(json, clazz);
      return Optional.of(value);
    } catch (Exception e) {
      log.error("Failed to deserialize JSON to object", e);
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }
}
