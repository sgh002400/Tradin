package com.traders.traders.module.users.controller.dto.request;

import javax.validation.constraints.NotBlank;

import com.traders.traders.module.users.service.dto.SubscribeStrategyDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SubscribeStrategyRequestDto {
	@NotBlank(message = "Binance Api Key must not be blank")
	private final String binanceApiKey;

	@NotBlank(message = "Binance Secret Key must not be blank")
	private final String binanceSecretKey;

	public SubscribeStrategyDto toServiceDto(Long id) {
		return SubscribeStrategyDto.of(id, binanceApiKey, binanceSecretKey);
	}
}
