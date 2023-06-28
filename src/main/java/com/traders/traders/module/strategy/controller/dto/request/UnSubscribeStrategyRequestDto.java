package com.traders.traders.module.strategy.controller.dto.request;

import com.traders.traders.module.strategy.service.dto.UnSubscribeStrategyDto;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class UnSubscribeStrategyRequestDto {
    @NotNull
    private final boolean isPositionClose;

    public UnSubscribeStrategyDto toServiceDto() {
        return new UnSubscribeStrategyDto(isPositionClose);
    }
}
