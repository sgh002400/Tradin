package com.traders.traders.module.strategy.domain.repository.dao;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import com.traders.traders.module.strategy.domain.TradingType;

import lombok.Getter;

@Getter
public class StrategyInfoDao {
	private final Long id;
	private final String name;
	private final double profitFactor;
	private final double netProfitRate;
	private final double winningRate;
	private final double totalProfitRate;
	private final double totalLossRate;
	private final double totalTradeCount;
	private final int winCount;
	private final int lossCount;
	private final TradingType tradingType;
	private final LocalDateTime time;
	private final int price;

	@QueryProjection
	public StrategyInfoDao(Long id, String name, double profitFactor, double netProfitRate, double winningRate,
		double totalProfitRate, double totalLossRate, double totalTradeCount, int winCount, int lossCount,
		TradingType tradingType, LocalDateTime time, int price) {
		this.id = id;
		this.name = name;
		this.profitFactor = profitFactor;
		this.netProfitRate = netProfitRate;
		this.winningRate = winningRate;
		this.totalProfitRate = totalProfitRate;
		this.totalLossRate = totalLossRate;
		this.totalTradeCount = totalTradeCount;
		this.winCount = winCount;
		this.lossCount = lossCount;
		this.tradingType = tradingType;
		this.time = time;
		this.price = price;
	}
}
