package com.traders.traders.module.users.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserSocialType {
	KAKAO("카카오"),
	GOOGLE("구글");

	private final String value;
}
