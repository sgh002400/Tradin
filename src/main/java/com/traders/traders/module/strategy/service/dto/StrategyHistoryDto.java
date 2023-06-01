package com.traders.traders.module.strategy.service.dto;

import java.util.List;

import com.traders.traders.module.history.domain.repository.dao.HistoryDao;
import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StrategyHistoryDto {
	private final StrategyInfoDao strategyInfoDao;
	private final List<HistoryDao> historyDaos;
}
