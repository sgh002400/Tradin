package com.tradin.module.users.controller.dto.request;

import com.tradin.module.strategy.domain.TradingType;
import com.tradin.module.users.service.dto.ChangeMetadataDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChangeMetadataRequestDto {
    private final int leverage;
    private final int quantityRate;
    private final TradingType tradingType;

    public ChangeMetadataDto toServiceDto() {
        return ChangeMetadataDto.of(leverage, quantityRate, tradingType);
    }
}
