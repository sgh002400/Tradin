package com.traders.traders.common.jwt;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.service.UsersService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
	private final JwtSecretKeyProvider jwtSecretKeyProvider;
	private final UsersService userService;
	private final RedisTemplate<String, Object> redisTemplate;

	public Long validateTokensAndGetUserId(String accessToken, String refreshToken) {
		validateTokenClaims(refreshToken);
		return getUserIdFromTokens(accessToken, refreshToken);
	}

	private void validateTokenClaims(String token) {
		parseClaim(token);
	}

	private Long getUserIdFromTokens(String accessToken, String refreshToken) {
		Long userId = validateAccessToken(accessToken);
		validateExistRefreshToken(refreshToken, userId);
		return userId;
	}

	private void validateExistRefreshToken(String refreshToken, Long userId) {
		Object refreshTokenFromDb = redisTemplate.opsForValue().get("RT:" + userId);

		if (refreshTokenFromDb == null) {
			throw new TradersException(NOT_FOUND_REFRESH_TOKEN_EXCEPTION);
		}

		if (!refreshToken.equals(refreshTokenFromDb)) {
			throw new TradersException(DIFFERENT_REFRESH_TOKEN_EXCEPTION);
		}
	}

	private Claims parseClaim(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(jwtSecretKeyProvider.getSecretKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (Exception e) {
			throw new TradersException(INVALID_JWT_TOKEN_EXCEPTION);
		}
	}

	public Long validateAccessToken(String accessToken) {
		Long userId = parseClaim(accessToken).get("USER_ID", Long.class);
		validateExistUserIdFromAccessToken(userId);
		return userId;
	}

	private void validateExistUserIdFromAccessToken(Long userId) {
		if (userId == null) {
			throw new TradersException(NOT_FOUND_JWT_USERID_EXCEPTION);
		}
	}

	public Authentication getAuthentication(Long userId) {
		Users user = userService.loadUserByUsername(String.valueOf(userId));
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}
}