package com.traders.traders.module.history.service.dto;

import com.traders.traders.module.strategy.domain.BackTestType;
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
    private BackTestType backTestType; //공매도, 공매수, 공매도 & 공매수

    public static BackTestDto of(Long id, String name, LocalDateTime startDate, LocalDateTime endDate,
                                 BackTestType backTestType) {
        return new BackTestDto(id, name, startDate, endDate, backTestType);
    }
}
