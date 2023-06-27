package com.traders.traders.module.strategy.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.traders.traders.module.strategy.domain.StrategyType;
import com.traders.traders.module.strategy.domain.repository.StrategyQueryRepository;
import com.traders.traders.module.strategy.domain.repository.dao.QStrategyInfoDao;
import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.traders.traders.module.strategy.domain.QStrategy.strategy;
import static com.traders.traders.module.strategy.domain.StrategyType.FUTURE;
import static com.traders.traders.module.strategy.domain.StrategyType.SPOT;

@RequiredArgsConstructor
public class StrategyQueryRepositoryImpl implements StrategyQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<StrategyInfoDao>> findFutureStrategiesInfoDao() {
        return getStrategyInfoDaos(FUTURE);
    }

    @Override
    public Optional<List<StrategyInfoDao>> findSpotStrategiesInfoDao() {
        return getStrategyInfoDaos(SPOT);
    }

    private Optional<List<StrategyInfoDao>> getStrategyInfoDaos(StrategyType strategyType) {
        List<StrategyInfoDao> strategyInfoDaos = jpaQueryFactory
                .select(
                        new QStrategyInfoDao(strategy.id, strategy.name, strategy.profitFactor, strategy.winningRate,
                                strategy.simpleProfitRate, strategy.compoundProfitRate, strategy.totalProfitRate,
                                strategy.totalLossRate, strategy.totalTradeCount, strategy.winCount, strategy.lossCount,
                                strategy.currentPosition.tradingType, strategy.currentPosition.time, strategy.currentPosition.price, strategy.averageHoldingPeriod, strategy.averageProfitRate))
                .from(strategy)
                .where(strategy.strategyType.eq(strategyType))
                .orderBy(strategy.id.asc())
                .fetch();

        return strategyInfoDaos.isEmpty() ? Optional.empty() : Optional.of(strategyInfoDaos);
    }

    @Override
    public List<StrategyInfoDao> findStrategyInfoDaoByNameIn(List<String> names) {
        return jpaQueryFactory
                .select(
                        new QStrategyInfoDao(strategy.id, strategy.name, strategy.profitFactor, strategy.winningRate,
                                strategy.simpleProfitRate, strategy.compoundProfitRate, strategy.totalProfitRate,
                                strategy.totalLossRate, strategy.totalTradeCount, strategy.winCount, strategy.lossCount,
                                strategy.currentPosition.tradingType, strategy.currentPosition.time, strategy.currentPosition.price, strategy.averageHoldingPeriod, strategy.averageProfitRate)
                )
                .from(strategy)
                .where(strategy.name.in(names))
                .orderBy(strategy.id.asc())
                .fetch();
    }
}
