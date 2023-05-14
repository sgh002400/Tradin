package com.traders.traders.module.strategy.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.common.utils.AESUtils;
import com.traders.traders.module.feign.service.FeignService;
import com.traders.traders.module.history.service.HistoryService;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.domain.Strategy;
import com.traders.traders.module.strategy.domain.repository.StrategyRepository;
import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.traders.traders.module.strategy.service.dto.WebHookDto;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.dao.AutoTradingSubscriberDao;
import com.traders.traders.module.users.service.UsersService;
import com.traders.traders.module.users.service.dto.SubscribeStrategyDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StrategyService {
	private final HistoryService historyService;
	private final FeignService feignService;
	private final UsersService userService;
	private final StrategyRepository strategyRepository;
	private final AESUtils aesUtils;

	public void handleWebHook(WebHookDto request) {
		Strategy strategy = findByName(request.getName()); //TODO - 예외가 안터짐

		autoTrading(strategy);
		closeHistory(strategy, request);
		createHistory(strategy, request);
		updateStrategy(strategy, request);
	}

	//TODO - 메시지는 생성됐는데 서버가 죽어서 처리를 못했을 때 테스트
	@Async //TODO - ("asyncTaskExecutor") 붙여야 적용되는지 확인
	public void autoTrading(Strategy strategy) {
		List<AutoTradingSubscriberDao> autoTradingSubscribers = userService.findAutoTradingSubscriber(
			strategy.getName());

		for (AutoTradingSubscriberDao autoTradingSubscriber : autoTradingSubscribers) {
			String apiKey = autoTradingSubscriber.getBinanceApiKey();
			String secretKey = autoTradingSubscriber.getBinanceSecretKey();
			//TODO - 전략은 롱, 유저는 숏일 때 테스트
			if (isCurrentLongPosition(strategy)) {
				feignService.closePosition(apiKey, secretKey, "SELL");
				feignService.createOrder(apiKey, secretKey, "BUY", autoTradingSubscriber.getQuantity());
			} else if (isCurrentShortPosition(strategy)) {
				feignService.closePosition(apiKey, secretKey, "BUY");
				feignService.createOrder(apiKey, secretKey, "SELL", autoTradingSubscriber.getQuantity());
			}
		}
	}

	public FindStrategiesInfoResponseDto findStrategiesInfo() {
		List<StrategyInfoDao> strategiesInfo = findStrategyInfoDaos();
		return new FindStrategiesInfoResponseDto(strategiesInfo);
	}

	public void subscribeStrategy(Users user, SubscribeStrategyDto request) {
		System.out.println("userId = " + user.getId());
		Strategy strategy = findStrategyById(request.getId());
		String encryptedApiKey = aesUtils.encrypt(request.getBinanceApiKey());
		String encryptedSecretKey = aesUtils.encrypt(request.getBinanceSecretKey());

		user.subscribeStrategy(strategy, encryptedApiKey, encryptedSecretKey);
	}

	private Strategy findStrategyById(Long id) {
		return strategyRepository.findById(id)
			.orElseThrow(() -> new TradersException(NOT_FOUND_STRATEGY_EXCEPTION));
	}

	private static boolean isCurrentLongPosition(Strategy strategy) {
		return strategy.isLongPosition();
	}

	private static boolean isCurrentShortPosition(Strategy strategy) {
		return strategy.isShortPosition();
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
