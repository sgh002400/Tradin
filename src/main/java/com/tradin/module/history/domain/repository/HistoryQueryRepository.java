package com.tradin.module.history.domain.repository;

import com.tradin.module.history.domain.repository.dao.HistoryDao;

import java.util.List;

public interface HistoryQueryRepository {
    List<HistoryDao> findHistoryDaoByStrategyId(Long id);
}
