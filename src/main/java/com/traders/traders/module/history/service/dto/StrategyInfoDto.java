package com.traders.traders.module.history.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StrategyInfoDto {
    private final Long id;
    private final String name;
    private final double compoundProfitRate;
    private final double winRate;
    private final double profitFactor;

    public static StrategyInfoDto of(Long id, String name, double compoundProfitRate, double winRate, double profitFactor) {
        return new StrategyInfoDto(id, name, compoundProfitRate, winRate, profitFactor);
    }
}
