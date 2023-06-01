package com.traders.traders.module.history.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.traders.traders.module.strategy.domain.Position;

import lombok.Getter;

@Getter
public class HistoryDao {
	private final Long id;
	private final Position entryPosition;
	private final Position exitPosition;
	private final double profitRate;

	@QueryProjection
	public HistoryDao(Long id, Position entryPosition, Position exitPosition, double profitRate) {
		this.id = id;
		this.entryPosition = entryPosition;
		this.exitPosition = exitPosition;
		this.profitRate = profitRate;
	}
}
