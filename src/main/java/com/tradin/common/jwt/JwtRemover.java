package com.tradin.common.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtRemover {
    private final RedisTemplate<String, Object> redisTemplate;

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete("RT:" + userId);
    }
}