package com.traders.traders.module.strategy.service.dto;

import java.util.List;

import com.traders.traders.module.history.domain.repository.dao.HistoryDao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StrategyHistoryDto {
	private final StrategyInfoDto strategyInfoDto;
	private final List<HistoryDao> historyDaos;
}
