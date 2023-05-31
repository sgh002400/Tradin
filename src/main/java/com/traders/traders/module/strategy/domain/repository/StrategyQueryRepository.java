package com.traders.traders.module.strategy.domain.repository;

import java.util.List;
import java.util.Optional;

import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;

public interface StrategyQueryRepository {
	Optional<List<StrategyInfoDao>> findStrategiesInfoDao();
}
