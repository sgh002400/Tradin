package com.traders.traders.common.secret;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "secret")
public final class SecretKeyManager {
	private final String jwt;
	private final Kakao kakao;

	public String getKakaoRestApiKey() {
		return kakao.getRestApiKey();
	}

	public String getKakaoRedirectUri() {
		return kakao.getRedirectUri();
	}

	@Getter
	@RequiredArgsConstructor
	public static final class Kakao {
		private final String restApiKey;
		private final String redirectUri;
	}

}