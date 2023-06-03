package com.traders.traders.module.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.traders.traders.module.feign.client.dto.KakaoProfile;

@FeignClient(name = "kakaoUserClient", url = "https://kapi.kakao.com")
public interface KakaoUserClient {

	@GetMapping("/v2/user/me")
	KakaoProfile getKakaoProfile(@RequestHeader("Authorization") String accessToken);
}