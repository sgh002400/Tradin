package com.traders.traders.module.users.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PingDto {
    private final String binanceApiKey;
    private final String binanceSecretKey;

    public static PingDto of(final String binanceApiKey, final String binanceSecretKey) {
        return new PingDto(binanceApiKey, binanceSecretKey);
    }
}
