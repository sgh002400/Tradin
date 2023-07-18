package com.tradin.module.strategy.domain.repository;

import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.domain.repository.dao.SubscriptionStrategyInfoDao;

import java.util.List;
import java.util.Optional;

public interface StrategyQueryRepository {
    Optional<List<StrategyInfoDao>> findFutureStrategiesInfoDao();

    Optional<List<SubscriptionStrategyInfoDao>> findSubscriptionStrategiesInfoDao();

    Optional<List<StrategyInfoDao>> findSpotStrategiesInfoDao();
}
