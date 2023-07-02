package com.tradin.module.strategy.domain.repository;

import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;

import java.util.List;
import java.util.Optional;

public interface StrategyQueryRepository {
    Optional<List<StrategyInfoDao>> findFutureStrategiesInfoDao();

    Optional<List<StrategyInfoDao>> findSpotStrategiesInfoDao();
}
