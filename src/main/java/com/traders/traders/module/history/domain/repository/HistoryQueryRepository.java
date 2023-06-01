package com.traders.traders.module.history.domain.repository;

import java.util.List;

import com.traders.traders.module.history.domain.repository.dao.HistoryDao;

public interface HistoryQueryRepository {
	List<HistoryDao> findHistoryDaosByStrategyId(Long strategyId);

}
