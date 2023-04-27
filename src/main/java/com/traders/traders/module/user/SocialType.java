package com.traders.traders.module.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SocialType {
	KAKAO("카카오"),
	GOOGLE("구글");

	private final String value;
}
