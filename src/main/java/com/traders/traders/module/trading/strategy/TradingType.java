package com.traders.traders.module.trading.strategy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TradingType {
	LONG("매수"),
	SHORT("매도");

	private final String value;
}
