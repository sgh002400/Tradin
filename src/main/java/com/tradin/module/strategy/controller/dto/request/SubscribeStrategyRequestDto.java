package com.tradin.module.strategy.controller.dto.request;

import com.tradin.module.strategy.domain.TradingType;
import com.tradin.module.users.service.dto.SubscribeStrategyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeStrategyRequestDto {
    @NotBlank(message = "Binance Api Key must not be blank")
    private String binanceApiKey;

    @NotBlank(message = "Binance Secret Key must not be blank")
    private String binanceSecretKey;

    @NotNull(message = "Leverage must not be null")
    @Min(value = 1, message = "레버리지는 최소 1이어야 합니다.")
    @Max(value = 125, message = "레버리지는 최대 125까지 가능합니다.")
    @Schema(description = "레버리지", example = "1 ~ 125")
    private int leverage;

    @NotNull(message = "QuantityRate must not be null")
    @Min(value = 1, message = "수량 비율은 최소 1이어야 합니다.")
    @Max(value = 100, message = "수량 비율은 최대 100까지 가능합니다.")
    private int quantityRate;

    @NotNull(message = "TradingType must not be null")
    private TradingType tradingType;

    public SubscribeStrategyDto toServiceDto(Long id) {
        return SubscribeStrategyDto.of(id, binanceApiKey, binanceSecretKey, leverage, quantityRate, tradingType);
    }
}
