package com.traders.traders.module.history.domain.repository;

import com.traders.traders.module.history.domain.repository.dao.HistoryDao;

import java.util.List;

public interface HistoryQueryRepository {
    List<HistoryDao> findHistoryDaoByStrategyId(Long id);
}
