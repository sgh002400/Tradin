package com.traders.traders.module.history.service.dto;

import com.traders.traders.module.strategy.domain.TradingType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class BackTestDto {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<TradingType> tradingTypes; //공매도, 공매수, 공매도 & 공매수

    public static BackTestDto of(Long id, String name, LocalDateTime startDate, LocalDateTime endDate,
                                 List<TradingType> tradingTypes) {
        return new BackTestDto(id, name, startDate, endDate, tradingTypes);
    }
}
