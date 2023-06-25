package com.traders.traders.module.strategy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StrategyInfoDto {
    private final Long id;
    private final double compoundProfitRate;
    private final double winRate;
    private final double profitFactor;

    public static StrategyInfoDto of(Long id, double compoundProfitRate, double winRate, double profitFactor) {
        return new StrategyInfoDto(id, compoundProfitRate, winRate, profitFactor);
    }
}
