package com.tradin.module.history.controller.dto.request;

import com.tradin.module.history.service.dto.BackTestDto;
import com.tradin.module.strategy.domain.TradingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class BackTestRequestDto {
    @NotNull(message = "StrategyId must not be null")
    private long id;

    @NotBlank(message = "StrategyName must not be blank")
    private String name;

    @NotNull(message = "StartDate must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "시작일", example = "2021-01-01T11:11:11")
    private LocalDateTime startDate;

    @NotNull(message = "EndDate must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "종료일", example = "2021-01-01T11:11:11")
    private LocalDateTime endDate;

    @Schema(description = "매매 타입", example = "LONG")
    @NotNull(message = "TradingType must not be null")
    private TradingType tradingType;

    public BackTestDto toServiceDto() {
        return BackTestDto.of(id, name, startDate, endDate, tradingType);
    }
}
