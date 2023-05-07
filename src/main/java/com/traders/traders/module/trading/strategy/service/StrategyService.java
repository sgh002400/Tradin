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
		saveHistory(request);
		createHistory(request);
		updateStrategy(request);
		autoTrading();
	}

	private void saveHistory(WebHookDto request) {
		historyService.saveHistory(request);
	}

	private void createHistory(WebHookDto request) {
		historyService.createHistory(request);
	}

	private void updateStrategy(WebHookDto request) {
		Strategy strategy = findByName(request.getName());

		updateMetaData(strategy, request);
		updateCurrentPosition(strategy, request);
	}

	private void updateCurrentPosition(Strategy strategy, WebHookDto request) {
		strategy.updateCurrentPosition(request.getPosition());
	}

	private void updateMetaData(Strategy strategy, WebHookDto request) {
		strategy.updateMetaData(request.getPosition());
	}

	//private 메서드는 비동기 안된다는듯?
	public void autoTrading() {
		//비동기적으로 자동매매
	}

	private Strategy findByName(String name) {
		return strategyRepository.findByName(name)
			.orElseThrow(() -> new TradersException(NOT_FOUND_STRATEGY_EXCEPTION));
	}
}
