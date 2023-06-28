package com.traders.traders.module.strategy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnSubscribeStrategyDto {
    private final boolean isPositionClose;

    public static UnSubscribeStrategyDto of(boolean isPositionClose) {
        return new UnSubscribeStrategyDto(isPositionClose);
    }
}
