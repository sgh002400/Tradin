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
import com.traders.traders.module.strategy.controller.dto.request.CreateStrategyDto;
import com.traders.traders.module.strategy.controller.dto.response.BackTestResponseDto;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.domain.Position;
import com.traders.traders.module.strategy.domain.Strategy;
import com.traders.traders.module.strategy.domain.repository.StrategyRepository;
import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.traders.traders.module.strategy.service.dto.BackTestDto;
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

	@Async //TODO - 동기 비동기 성능 차이 블로그 작성하기
	public void handleWebHook(WebHookDto request) {
		Strategy strategy = findByName(request.getName());

		autoTrading(strategy);
		closeOngoingHistory(strategy, request.getPosition());
		createNewHistory(strategy, request.getPosition());
		updateStrategyMetaData(strategy, request.getPosition());
	}

	@Async
	public void autoTrading(Strategy strategy) {
		List<AutoTradingSubscriberDao> autoTradingSubscribers = userService.findAutoTradingSubscriberByStrategyName(
			strategy.getName());

		for (AutoTradingSubscriberDao autoTradingSubscriber : autoTradingSubscribers) {
			String apiKey = getDecryptedKey(autoTradingSubscriber.getBinanceApiKey());
			String secretKey = getDecryptedKey(autoTradingSubscriber.getBinanceSecretKey());

			switchPosition(strategy, apiKey, secretKey, autoTradingSubscriber.getQuantity());
		}
	}

	public FindStrategiesInfoResponseDto findStrategiesInfo() {
		List<StrategyInfoDao> strategiesInfo = findStrategyInfoDaos();
		return new FindStrategiesInfoResponseDto(strategiesInfo);
	}

	public void subscribeStrategy(SubscribeStrategyDto request) {
		Users savedUser = getUserFromSecurityContext();
		Strategy strategy = findById(request.getId());
		String encryptedApiKey = getEncryptedKey(request.getBinanceApiKey());
		String encryptedSecretKey = getEncryptedKey(request.getBinanceSecretKey());

		savedUser.subscribeStrategy(strategy, encryptedApiKey, encryptedSecretKey);
	}

	public BackTestResponseDto backTest(BackTestDto request) {
		//캐시에 매매 내역들 있는지 확인

		//없다면 매매 내역 전체 캐싱

		//매매 내역들을 조건에 맞게 (기간, 자기 자본 100) 계산 후 응답

		return null;
	}

	public void createStrategy(CreateStrategyDto request) {
		Strategy strategy = Strategy.of(request.getName(), request.getProfitFactor(), request.getWinningRate(),
			request.getSimpleProfitRate(), request.getCompoundProfitRate(), request.getTotalProfitRate(),
			request.getTotalLossRate(), request.getWinCount(), request.getLossCount(), request.getCurrentPosition());

		strategyRepository.save(strategy);
	}

	private void switchPosition(Strategy strategy, String apiKey, String secretKey, double orderQuantity) {
		if (isCurrentLongPosition(strategy)) {
			feignService.switchPosition(apiKey, secretKey, "SELL", orderQuantity);
		} else if (isCurrentShortPosition(strategy)) {
			feignService.switchPosition(apiKey, secretKey, "BUY", orderQuantity);
		}
	}

	private String getEncryptedKey(String key) {
		return aesUtils.encrypt(key);
	}

	private String getDecryptedKey(String encryptedKey) {
		return aesUtils.decrypt(encryptedKey);
	}

	private Users getUserFromSecurityContext() {
		return userService.getUserFromSecurityContext();
	}

	private Strategy findById(Long id) {
		return strategyRepository.findById(id)
			.orElseThrow(() -> new TradersException(NOT_FOUND_STRATEGY_EXCEPTION));
	}

	private static boolean isCurrentLongPosition(Strategy strategy) {
		return strategy.isLongPosition();
	}

	private static boolean isCurrentShortPosition(Strategy strategy) {
		return strategy.isShortPosition();
	}

	private void closeOngoingHistory(Strategy strategy, Position position) {
		historyService.closeOngoingHistory(strategy, position);
	}

	private void createNewHistory(Strategy strategy, Position position) {
		historyService.createNewHistory(strategy, position);
	}

	private void updateStrategyMetaData(Strategy strategy, Position position) {
		strategy.updateMetaData(position);
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
