package com.traders.traders.module.strategy.controller.dto.request;

import com.traders.traders.module.strategy.domain.Position;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateStrategyDto {
	private final String name;
	private final double profitFactor;
	private final double netProfitRate;
	private final double winningRate;
	private final double totalProfitRate;
	private final double totalLossRate;
	private final double totalTradeCount;
	private final int winCount;
	private final int lossCount;
	private final Position currentPosition;
}
