package com.traders.traders.module.strategy.service.dto;

import java.util.List;

import com.traders.traders.module.history.domain.History;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HistoryCache {
	private final List<History> histories;

	public static HistoryCache of(List<History> histories) {
		return new HistoryCache(histories);
	}
}
