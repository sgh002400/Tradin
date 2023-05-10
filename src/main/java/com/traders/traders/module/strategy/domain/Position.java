package com.traders.traders.module.strategy.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Position {
	@Enumerated(EnumType.STRING)
	private TradingType tradingType;

	@Column(nullable = false)
	private LocalDateTime time;

	@Column(nullable = false)
	private int price;

	@Builder
	private Position(TradingType tradingType, LocalDateTime time, int price) {
		this.tradingType = tradingType;
		this.time = time;
		this.price = price;
	}

	public static Position of(TradingType tradingType, LocalDateTime time, int price) {
		return Position.builder()
			.tradingType(tradingType)
			.time(time)
			.price(price)
			.build();
	}
}
