package com.traders.traders.module.trading.strategy;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.Builder;
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

	@Builder
	private Position(TradingType tradingType, LocalDateTime time, String price) {
		this.tradingType = tradingType;
		this.time = time;
		this.price = price;
	}

	public static Position of(TradingType tradingType, LocalDateTime time, String price) {
		return Position.builder()
			.tradingType(tradingType)
			.time(time)
			.price(price)
			.build();
	}
}
