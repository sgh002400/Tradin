package com.traders.traders.module.history.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.history.domain.History;
import com.traders.traders.module.history.domain.repository.HistoryRepository;
import com.traders.traders.module.strategy.domain.Position;
import com.traders.traders.module.strategy.domain.Strategy;
import com.traders.traders.module.strategy.service.dto.WebHookDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
	private final HistoryRepository historyRepository;

	public void closeHistory(Strategy strategy, WebHookDto request) {
		History history = findLastHistoryByStrategyId(strategy.getId());
		closeOpenPosition(history, request.getPosition());
		calculateProfitRate(history); //TODO - history.exitPosition 잘 되는지랑 수치 계산 잘 되는지 테스트 해보기
	}

	public void createHistory(Strategy strategy, WebHookDto request) {
		History.of(request.getPosition(), strategy);
	}

	private static void calculateProfitRate(History history) {
		history.calculateProfitRate();
	}

	private static void closeOpenPosition(History history, Position position) {
		history.closeOpenPosition(position);
	}

	private History findLastHistoryByStrategyId(Long id) {
		return historyRepository.findLastHistoryByStrategyId(id)
			.orElseThrow(() -> new TradersException(NOT_FOUND_OPEN_POSITION_EXCEPTION));
	}
}
