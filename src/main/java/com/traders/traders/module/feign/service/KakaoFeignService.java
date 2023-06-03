package com.traders.traders.module.feign.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.secret.SecretKeyManager;
import com.traders.traders.common.utils.HttpHeaderUtils;
import com.traders.traders.module.feign.client.KakaoAuthorizationClient;
import com.traders.traders.module.feign.client.KakaoUserClient;
import com.traders.traders.module.feign.client.dto.KakaoBearerTokenRequest;
import com.traders.traders.module.feign.client.dto.KakaoBearerTokenResponse;
import com.traders.traders.module.feign.client.dto.KakaoProfile;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoFeignService {
	private final SecretKeyManager secretKeyManager;
	private final KakaoUserClient kakaoUserClient;
	private final KakaoAuthorizationClient kakaoAuthorizationClient;

	public KakaoProfile getKakaoProfile(final String authorizationCode) {
		String accessToken = getKakaoAccessToken(authorizationCode);

		return kakaoUserClient.getKakaoProfile(HttpHeaderUtils.concatWithBearerPrefix(accessToken));
	}

	public String getKakaoAccessToken(final String authorizationCode) {
		final String KAKAO_API_KEY = secretKeyManager.getKakaoRestApiKey();
		final String KAKAO_REDIRECT_URI = secretKeyManager.getKakaoRedirectUri();

		KakaoBearerTokenResponse kakaoBearerTokenResponse = kakaoAuthorizationClient.getBearerToken(
			KakaoBearerTokenRequest.of(KAKAO_API_KEY, KAKAO_REDIRECT_URI, authorizationCode));

		return kakaoBearerTokenResponse.getAccessToken();
	}

}
