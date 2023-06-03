package com.traders.traders.module.feign.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class KakaoBearerTokenRequest {
	private static final String GRANT_TYPE = "authorization_code";

	private String grant_type;
	private String client_id;
	private String redirect_uri;
	private String code;
	private String client_secret;

	public static KakaoBearerTokenRequest of(String clientId, String redirectUri, String code) {
		return new KakaoBearerTokenRequest(GRANT_TYPE, clientId, redirectUri, code, null);
	}
}
