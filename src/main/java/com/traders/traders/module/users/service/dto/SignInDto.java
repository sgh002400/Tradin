package com.traders.traders.module.users.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignInDto {
	private String email;
	private String password;

	public static SignInDto of(final String accessToken, final String refreshToken) {
		return new SignInDto(accessToken, refreshToken);
	}
}
