package com.tradin.module.users.controller.dto.request;

import com.tradin.module.users.service.dto.PingDto;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
public class PingRequestDto {
    @NotBlank(message = "Binance Api Key must not be blank")
    private final String binanceApiKey;

    @NotBlank(message = "Binance Secret Key must not be blank")
    private final String binanceSecretKey;

    public PingDto toServiceDto() {
        return PingDto.of(binanceApiKey, binanceSecretKey);
    }
}
