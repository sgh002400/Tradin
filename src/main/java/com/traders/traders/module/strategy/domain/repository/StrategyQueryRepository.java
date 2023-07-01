package com.traders.traders.module.strategy.domain.repository;

import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;

import java.util.List;
import java.util.Optional;

public interface StrategyQueryRepository {
    Optional<List<StrategyInfoDao>> findFutureStrategiesInfoDao();

    Optional<List<StrategyInfoDao>> findSpotStrategiesInfoDao();
}
