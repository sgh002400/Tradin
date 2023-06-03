package com.traders.traders.module.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.traders.traders.module.feign.client.dto.KakaoBearerTokenRequest;
import com.traders.traders.module.feign.client.dto.KakaoBearerTokenResponse;

@FeignClient(name = "kakaoAuthorizationClient", url = "https://kauth.kakao.com")
public interface KakaoAuthorizationClient {
	@PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
	KakaoBearerTokenResponse getBearerToken(@RequestBody KakaoBearerTokenRequest kakaoBearerTokenRequest);
}
