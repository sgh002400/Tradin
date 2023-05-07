package com.traders.traders.module.trading.history.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.trading.history.domain.History;
import com.traders.traders.module.trading.history.domain.repository.HistoryRepository;
import com.traders.traders.module.trading.strategy.domain.Strategy;
import com.traders.traders.module.trading.strategy.service.dto.WebHookDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
	private final HistoryRepository historyRepository;

	public void closeHistory(Strategy strategy, WebHookDto request) {
		History history = findLastHistoryByStrategyId(strategy.getId());
		history.closeOpenPosition(request.getPosition());
	}

	public void createHistory(Strategy strategy, WebHookDto request) {
		History.of(request.getPosition(), null, strategy);
	}

	private History findLastHistoryByStrategyId(Long id) {
		return historyRepository.findLastHistoryByStrategyId(id)
			.orElseThrow(() -> new TradersException(NOT_FOUND_OPEN_POSITION_EXCEPTION));
	}
}
