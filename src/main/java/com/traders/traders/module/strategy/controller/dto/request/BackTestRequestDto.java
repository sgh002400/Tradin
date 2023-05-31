package com.traders.traders.module.strategy.controller.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.traders.traders.module.strategy.domain.BackTestType;
import com.traders.traders.module.strategy.domain.OrderSize;
import com.traders.traders.module.strategy.service.dto.BackTestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BackTestRequestDto {
	/**
	 * 최대 N개의 전략을 동시에 선택하여 결과를 비교할 수 있음
	 * 기간을 선택할 수 있음
	 * 자기자본 100%, 매번 고정 금액 트레이딩 선택할 수 있음
	 * 공매도, 공매수, 공매도&공매수 선택할 수 있음
	 */

	private final List<String> names;
	private final LocalDateTime startDate;
	private final LocalDateTime endDate;
	private final OrderSize orderSize; //자기자본 100%, 매번 고정 금액 트레이딩
	private final BackTestType backTestType; //공매도, 공매수, 공매도 & 공매수

	public BackTestDto toServiceDto() {
		return BackTestDto.of(names, startDate, endDate, orderSize, backTestType);
	}
}
