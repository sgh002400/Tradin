package com.traders.traders.module.trading.strategy;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.traders.traders.common.jpa.AuditTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Strategy extends AuditTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private double profitFactor;

	@Column(nullable = false)
	private double profitRate;

	@Column(nullable = false)
	private double winningRate;

	@Embedded
	private Position currentPosition;

	@Builder
	private Strategy(String name, double profitFactor, double profitRate, double winningRate,
		Position currentPosition) {
		this.name = name;
		this.profitFactor = profitFactor;
		this.profitRate = profitRate;
		this.winningRate = winningRate;
		this.currentPosition = currentPosition;
	}

	public static Strategy of(String name, double profitFactor, double profitRate, double winningRate,
		Position currentPosition) {
		return Strategy.builder()
			.name(name)
			.profitFactor(profitFactor)
			.profitRate(profitRate)
			.winningRate(winningRate)
			.currentPosition(currentPosition)
			.build();
	}
}
