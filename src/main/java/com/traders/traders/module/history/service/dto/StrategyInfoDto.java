package com.traders.traders.module.history.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StrategyInfoDto {
    private final Long id;
    private final String name;
    private final double compoundProfitRate;
    private final double winRate;
    private final double profitFactor;
    private final int totalTradeCount;
    private final double averageProfitRate;

    @Builder
    public StrategyInfoDto(Long id, String name, double compoundProfitRate, double winRate, double profitFactor, int totalTradeCount, double averageProfitRate) {
        this.id = id;
        this.name = name;
        this.compoundProfitRate = compoundProfitRate;
        this.winRate = winRate;
        this.profitFactor = profitFactor;
        this.totalTradeCount = totalTradeCount;
        this.averageProfitRate = averageProfitRate;
    }
}
