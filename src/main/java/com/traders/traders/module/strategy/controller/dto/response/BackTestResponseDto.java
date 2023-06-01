package com.traders.traders.module.strategy.controller.dto.response;

import java.util.List;

import com.traders.traders.module.strategy.service.dto.StrategyHistoryDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BackTestResponseDto {
	private final List<StrategyHistoryDto> strategyHistoryDtos;

	public static BackTestResponseDto of(List<StrategyHistoryDto> strategyHistoryDtos) {
		return new BackTestResponseDto(strategyHistoryDtos);
	}
}
