package com.traders.traders.module.strategy.service.dto;

import java.util.List;

import com.traders.traders.module.history.domain.repository.dao.HistoryDao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HistoryCache {
	private final List<HistoryDao> histories;

	public static HistoryCache of(List<HistoryDao> histories) {
		return new HistoryCache(histories);
	}
}
