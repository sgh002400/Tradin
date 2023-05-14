package com.traders.traders.module.users.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class AutoTradingSubscriberDao {
	private final int leverage;
	private final long quantity;
	private final String binanceApiKey;
	private final String binanceSecretKey;

	@QueryProjection
	public AutoTradingSubscriberDao(int leverage, long quantity, String binanceApiKey, String binanceSecretKey) {
		this.leverage = leverage;
		this.quantity = quantity;
		this.binanceApiKey = binanceApiKey;
		this.binanceSecretKey = binanceSecretKey;
	}
}
