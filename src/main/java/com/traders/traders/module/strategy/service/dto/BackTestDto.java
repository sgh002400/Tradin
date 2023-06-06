package com.traders.traders.module.strategy.service.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.traders.traders.module.strategy.domain.BackTestType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BackTestDto {
	private List<String> names;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private BackTestType backTestType; //공매도, 공매수, 공매도 & 공매수

	public static BackTestDto of(List<String> name, LocalDateTime startDate, LocalDateTime endDate,
		BackTestType backTestType) {
		return new BackTestDto(name, startDate, endDate, backTestType);
	}
}
