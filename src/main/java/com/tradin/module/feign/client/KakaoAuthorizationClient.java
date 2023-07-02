package com.tradin.module.feign.client;

import com.tradin.module.feign.client.dto.KakaoBearerTokenRequest;
import com.tradin.module.feign.client.dto.KakaoBearerTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoAuthorizationClient", url = "https://kauth.kakao.com")
public interface KakaoAuthorizationClient {
    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoBearerTokenResponse getBearerToken(@RequestBody KakaoBearerTokenRequest kakaoBearerTokenRequest);
}
