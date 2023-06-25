package com.traders.traders.module.strategy.service.dto;

import com.traders.traders.module.strategy.domain.BackTestType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class BackTestDto {
    private List<Long> ids;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BackTestType backTestType; //공매도, 공매수, 공매도 & 공매수

    public static BackTestDto of(List<Long> ids, LocalDateTime startDate, LocalDateTime endDate,
                                 BackTestType backTestType) {
        return new BackTestDto(ids, startDate, endDate, backTestType);
    }
}
