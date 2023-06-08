package com.traders.traders.module.strategy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StrategyInfoDto {
	private final String name;
	private final double compoundProfitRate;
	private final double winRate;
	private final double profitFactor;

	public static StrategyInfoDto of(String name, double compoundProfitRate, double winRate, double profitFactor) {
		return new StrategyInfoDto(name, compoundProfitRate, winRate, profitFactor);
	}
}
