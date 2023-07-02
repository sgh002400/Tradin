package com.tradin.module.strategy.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.strategy.domain.StrategyType;
import com.tradin.module.strategy.domain.repository.StrategyQueryRepository;
import com.tradin.module.strategy.domain.repository.dao.QStrategyInfoDao;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.tradin.module.strategy.domain.QStrategy.strategy;
import static com.tradin.module.strategy.domain.StrategyType.FUTURE;
import static com.tradin.module.strategy.domain.StrategyType.SPOT;

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
                        new QStrategyInfoDao(
                                strategy.id,
                                strategy.name,
                                strategy.profitFactor,
                                strategy.rate.winningRate,
                                strategy.rate.simpleProfitRate,
                                strategy.rate.compoundProfitRate,
                                strategy.rate.totalProfitRate,
                                strategy.rate.totalLossRate,
                                strategy.rate.averageProfitRate,
                                strategy.count.totalTradeCount,
                                strategy.count.winCount,
                                strategy.count.lossCount,
                                strategy.currentPosition.tradingType,
                                strategy.currentPosition.time,
                                strategy.currentPosition.price,
                                strategy.averageHoldingPeriod
                        ))
                .from(strategy)
                .where(strategy.type.strategyType.eq(strategyType))
                .orderBy(strategy.id.asc())
                .fetch();

        return strategyInfoDaos.isEmpty() ? Optional.empty() : Optional.of(strategyInfoDaos);
    }
}
