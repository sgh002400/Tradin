package com.traders.traders.module.feign.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class KakaoBearerTokenResponse {
	private String accessToken;
	private String tokenType;
	private String refreshToken;
	private int expiresIn;
	private String scope;
	private String refreshTokenExpiresIn;
}
