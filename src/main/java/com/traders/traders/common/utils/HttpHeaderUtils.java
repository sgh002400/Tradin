package com.traders.traders.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeaderUtils {
	public static final String BEARER_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_HEADER_PREFIX = "Authorization";
	public static final String KAKAO_AK_PREFIX = "KakaoAK ";

	public static String concatWithBearerPrefix(String token) {
		return BEARER_PREFIX.concat(token);
	}

	public static String concatWithKakaoAKPrefix(String restApiKey) {
		return KAKAO_AK_PREFIX.concat(restApiKey);
	}
}