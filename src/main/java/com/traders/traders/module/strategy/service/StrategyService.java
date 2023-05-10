package com.traders.traders.module.strategy.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.feign.client.dto.NewOrderDto;
import com.traders.traders.module.feign.service.FeignService;
import com.traders.traders.module.history.service.HistoryService;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.domain.Strategy;
import com.traders.traders.module.strategy.domain.repository.StrategyRepository;
import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.traders.traders.module.strategy.service.dto.WebHookDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StrategyService {

	private final HistoryService historyService;
	private final FeignService feignService;
	private final StrategyRepository strategyRepository;

	public void handleWebHook(WebHookDto request) {
		autoTrading();

		// Strategy strategy = findByName(request.getName());
		// closeHistory(strategy, request);
		// createHistory(strategy, request);
		// updateStrategy(strategy, request);
	}

	public void autoTrading() {
		//유저 정보 조회
		String apiKey = "tmp";
		String secretKey = "tmp";

		//현재 포지션이 Long이면 Short, Short이면 Long으로 포지션 종료
		NewOrderDto newOrderDto = feignService.createOrder(apiKey, secretKey, "BUY", "1");

		//현재 포지션이 없으면 웹 훅의 포지션에 따라 포지션 진입

	}

	public FindStrategiesInfoResponseDto findStrategiesInfo() {
		List<StrategyInfoDao> strategiesInfo = findStrategyInfoDaos();
		return new FindStrategiesInfoResponseDto(strategiesInfo);
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
