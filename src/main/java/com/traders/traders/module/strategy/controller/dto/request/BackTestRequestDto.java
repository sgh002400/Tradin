package com.traders.traders.module.strategy.controller.dto.request;

import com.traders.traders.module.strategy.domain.BackTestType;
import com.traders.traders.module.strategy.service.dto.BackTestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class BackTestRequestDto {
    private final List<Long> ids;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final BackTestType backTestType;

    public BackTestDto toServiceDto() {
        return BackTestDto.of(ids, startDate, endDate, backTestType);
    }
}
