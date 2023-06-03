package com.traders.traders.module.users.controller.dto;

import com.traders.traders.module.users.controller.dto.response.TokenResponseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignInResponseDto {
	private Long id;
	private TokenResponseDto token;

	public static SignInResponseDto of(Long userId, final TokenResponseDto token) {
		return new SignInResponseDto(userId, token);
	}
}
