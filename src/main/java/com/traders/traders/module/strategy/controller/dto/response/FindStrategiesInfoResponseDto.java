package com.traders.traders.module.strategy.controller.dto.response;

import java.util.List;

import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindStrategiesInfoResponseDto {
	private final List<StrategyInfoDao> strategiesInfo;
}
