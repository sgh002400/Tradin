package com.traders.traders.module.users.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SubscribeStrategyDto {
	private Long id;
	private String binanceApiKey;
	private String binanceSecretKey;

	public static SubscribeStrategyDto of(final Long id, final String binanceApiKey,
		final String binanceSecretKey) {
		return new SubscribeStrategyDto(id, binanceApiKey, binanceSecretKey);
	}
}
