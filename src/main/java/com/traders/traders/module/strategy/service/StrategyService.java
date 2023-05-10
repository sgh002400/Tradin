package com.traders.traders.module.strategy.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.history.service.HistoryService;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.domain.Strategy;
import com.traders.traders.module.strategy.domain.repository.StrategyRepository;
import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.traders.traders.module.strategy.service.dto.WebHookDto;

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

	public FindStrategiesInfoResponseDto findStrategiesInfo() {
		List<StrategyInfoDao> strategiesInfo = findStrategyInfoDaos();
		return new FindStrategiesInfoResponseDto(strategiesInfo);
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

	private List<StrategyInfoDao> findStrategyInfoDaos() {
		return strategyRepository.findStrategiesInfoDao()
			.orElseThrow(() -> new TradersException(NOT_FOUND_ANY_STRATEGY_EXCEPTION));
	}
}
