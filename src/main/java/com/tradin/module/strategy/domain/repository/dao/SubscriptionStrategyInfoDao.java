package com.tradin.module.strategy.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.tradin.module.strategy.domain.CoinType;
import lombok.Getter;

@Getter
public class SubscriptionStrategyInfoDao {
    private final long id;
    private final String name;
    private final CoinType coinType;
    private final double profitFactor;
    private final double winningRate;
    private final double compoundProfitRate;
    private final double averageProfitRate;

    @QueryProjection
    public SubscriptionStrategyInfoDao(long id, String name, CoinType coinType, double profitFactor, double winningRate, double compoundProfitRate, double averageProfitRate) {
        this.id = id;
        this.name = name;
        this.coinType = coinType;
        this.profitFactor = profitFactor;
        this.winningRate = winningRate;
        this.compoundProfitRate = compoundProfitRate;
        this.averageProfitRate = averageProfitRate;
    }
}
