package com.traders.traders.module.history.controller.dto.request;

import com.traders.traders.module.history.service.dto.BackTestDto;
import com.traders.traders.module.strategy.domain.TradingType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class BackTestRequestDto {
    private final Long id;
    private final String name;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final List<TradingType> tradingTypes;

    public BackTestDto toServiceDto() {
        return BackTestDto.of(id, name, startDate, endDate, tradingTypes);
    }
}
