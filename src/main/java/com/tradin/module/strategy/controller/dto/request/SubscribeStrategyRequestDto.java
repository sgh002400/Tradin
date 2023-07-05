package com.tradin.module.strategy.controller.dto.request;

import com.tradin.module.users.service.dto.SubscribeStrategyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeStrategyRequestDto {
    @NotBlank(message = "Binance Api Key must not be blank")
    private String binanceApiKey;

    @NotBlank(message = "Binance Secret Key must not be blank")
    private String binanceSecretKey;

    public SubscribeStrategyDto toServiceDto(Long id) {
        return SubscribeStrategyDto.of(id, binanceApiKey, binanceSecretKey);
    }
}
