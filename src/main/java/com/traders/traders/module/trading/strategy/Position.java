package com.traders.traders.module.trading.strategy;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position {
	@Enumerated(EnumType.STRING)
	private TradingType tradingType;

	@Column(nullable = false)
	private LocalDateTime time;

	@Column(nullable = false)
	private String price;
}
