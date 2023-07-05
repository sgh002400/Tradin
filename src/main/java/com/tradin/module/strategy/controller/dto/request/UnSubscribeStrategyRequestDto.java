package com.tradin.module.strategy.controller.dto.request;

import com.tradin.module.strategy.service.dto.UnSubscribeStrategyDto;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class UnSubscribeStrategyRequestDto {
    @NotNull(message = "isPositionClose must not be null")
    private final boolean isPositionClose;

    public UnSubscribeStrategyDto toServiceDto() {
        return new UnSubscribeStrategyDto(isPositionClose);
    }
}
