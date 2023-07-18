package com.tradin.module.strategy.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.tradin.module.strategy.domain.CoinType;
import com.tradin.module.strategy.domain.TradingType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StrategyInfoDao {
    private final Long id;
    private final String name;
    private final CoinType coinType;
    private final double profitFactor;
    private final double winningRate;
    private final double simpleProfitRate;
    private final double compoundProfitRate;
    private final double totalProfitRate;
    private final double totalLossRate;
    private final double averageProfitRate;
    private final int totalTradeCount;
    private final int winCount;
    private final int lossCount;
    private final TradingType tradingType;
    private final LocalDateTime time;
    private final int price;
    private final int averageHoldingPeriod;

    @QueryProjection
    public StrategyInfoDao(Long id, String name, CoinType coinType, double profitFactor, double winningRate, double simpleProfitRate, double compoundProfitRate, double totalProfitRate, double totalLossRate, double averageProfitRate, int totalTradeCount, int winCount, int lossCount, TradingType tradingType, LocalDateTime time, int price, int averageHoldingPeriod) {
        this.id = id;
        this.name = name;
        this.coinType = coinType;
        this.profitFactor = profitFactor;
        this.winningRate = winningRate;
        this.simpleProfitRate = simpleProfitRate;
        this.compoundProfitRate = compoundProfitRate;
        this.totalProfitRate = totalProfitRate;
        this.totalLossRate = totalLossRate;
        this.averageProfitRate = averageProfitRate;
        this.totalTradeCount = totalTradeCount;
        this.winCount = winCount;
        this.lossCount = lossCount;
        this.tradingType = tradingType;
        this.time = time;
        this.price = price;
        this.averageHoldingPeriod = averageHoldingPeriod;
    }
}
