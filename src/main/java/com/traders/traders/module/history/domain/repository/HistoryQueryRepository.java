package com.traders.traders.module.history.domain.repository;

import java.util.List;

import com.traders.traders.module.history.domain.History;

public interface HistoryQueryRepository {
	List<History> findHistoriesByStrategyName(String name);
}
