package com.tradin.module.history.controller.dto.request;

import com.tradin.module.history.service.dto.BackTestDto;
import com.tradin.module.strategy.domain.TradingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class BackTestRequestDto {
    @NotNull(message = "StrategyId must not be null")
    private final Long id;

    @NotBlank(message = "StrategyName must not be blank")
    private final String name;

    @NotNull(message = "StartDate must not be null")
    private final LocalDateTime startDate;

    @NotNull(message = "EndDate must not be null")
    private final LocalDateTime endDate;

    @Schema(description = "매매 타입", example = "LONG, SHORT, BOTH")
    @NotNull(message = "TradingType must not be null")
    private final TradingType tradingType;

    public BackTestDto toServiceDto() {
        return BackTestDto.of(id, name, startDate, endDate, tradingType);
    }
}
