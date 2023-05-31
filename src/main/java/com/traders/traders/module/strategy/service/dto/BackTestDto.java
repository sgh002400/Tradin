package com.traders.traders.module.strategy.service.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.traders.traders.module.strategy.domain.BackTestType;
import com.traders.traders.module.strategy.domain.OrderSize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BackTestDto {
	private List<String> names;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private OrderSize orderSize; //자기자본 100%, 매번 고정 금액 트레이딩
	private BackTestType backTestType; //공매도, 공매수, 공매도 & 공매수

	public static BackTestDto of(List<String> name, LocalDateTime startDate, LocalDateTime endDate, OrderSize orderSize,
		BackTestType backTestType) {
		return new BackTestDto(name, startDate, endDate, orderSize, backTestType);
	}
}
