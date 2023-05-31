package com.traders.traders.module.strategy.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderSize {
	PERCENTAGE("퍼센트");

	private final String value;
}
