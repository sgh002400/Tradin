package com.traders.traders.module.strategy.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BackTestType {
	LONG("매수"),
	SHORT("매도"),
	BOTH("매수&매도");

	private final String value;
}
