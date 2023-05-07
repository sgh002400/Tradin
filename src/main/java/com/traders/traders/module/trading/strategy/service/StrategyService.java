package com.traders.traders.module.trading.strategy.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.trading.history.service.HistoryService;
import com.traders.traders.module.trading.strategy.domain.Strategy;
import com.traders.traders.module.trading.strategy.domain.repository.StrategyRepository;
import com.traders.traders.module.trading.strategy.service.dto.WebHookDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StrategyService {

	private final HistoryService historyService;
	private final StrategyRepository strategyRepository;

	public void handleWebHook(WebHookDto request) {
		Strategy strategy = findByName(request.getName());

		closeHistory(strategy, request);
		createHistory(strategy, request);
		updateStrategy(strategy, request);
		autoTrading();
	}

	//private 메서드는 비동기 안된다는듯?
	public void autoTrading() {
		//비동기적으로 자동매매
	}

	private void closeHistory(Strategy strategy, WebHookDto request) {
		historyService.closeHistory(strategy, request);
	}

	private void createHistory(Strategy strategy, WebHookDto request) {
		historyService.createHistory(strategy, request);
	}

	private void updateStrategy(Strategy strategy, WebHookDto request) {
		strategy.updateMetaData(request.getPosition());
	}

	private Strategy findByName(String name) {
		return strategyRepository.findByName(name)
			.orElseThrow(() -> new TradersException(NOT_FOUND_STRATEGY_EXCEPTION));
	}
}
