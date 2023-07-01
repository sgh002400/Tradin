package com.traders.traders.module.history.service.dto;

import com.traders.traders.module.strategy.domain.TradingType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class BackTestDto {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TradingType tradingType;

    public static BackTestDto of(Long id, String name, LocalDateTime startDate, LocalDateTime endDate,
                                 TradingType tradingType) {
        return new BackTestDto(id, name, startDate, endDate, tradingType);
    }
}
