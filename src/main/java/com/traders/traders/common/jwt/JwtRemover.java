package com.traders.traders.common.jwt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRemover {
	private final RedisTemplate<String, Object> redisTemplate;

	public void deleteRefreshToken(Long userId) {
		redisTemplate.delete("RT:" + userId);
	}
}