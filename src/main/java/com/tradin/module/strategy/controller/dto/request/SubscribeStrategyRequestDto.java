package com.tradin.module.strategy.controller.dto.request;

import com.tradin.module.users.service.dto.SubscribeStrategyDto;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
public class SubscribeStrategyRequestDto {
    @NotBlank(message = "Binance Api Key must not be blank")
    private final String binanceApiKey;

    @NotBlank(message = "Binance Secret Key must not be blank")
    private final String binanceSecretKey;

    public SubscribeStrategyDto toServiceDto(Long id) {
        return SubscribeStrategyDto.of(id, binanceApiKey, binanceSecretKey);
    }
}
