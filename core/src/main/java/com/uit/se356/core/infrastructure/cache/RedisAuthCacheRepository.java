package com.uit.se356.core.infrastructure.cache;

import com.uit.se356.core.application.authentication.port.AuthCacheRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisAuthCacheRepository implements AuthCacheRepository {

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
}
