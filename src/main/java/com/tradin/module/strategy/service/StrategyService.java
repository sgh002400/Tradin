package com.tradin.module.strategy.service;

import com.tradin.common.exception.ExceptionMessage;
import com.tradin.common.exception.TradinException;
import com.tradin.common.utils.AESUtils;
import com.tradin.module.feign.service.BinanceFeignService;
import com.tradin.module.history.service.HistoryService;
import com.tradin.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.domain.Position;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.domain.TradingType;
import com.tradin.module.strategy.domain.repository.StrategyRepository;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.service.dto.UnSubscribeStrategyDto;
import com.tradin.module.strategy.service.dto.WebHookDto;
import com.tradin.module.trade.service.TradeService;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.service.UsersService;
import com.tradin.module.users.service.dto.SubscribeStrategyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StrategyService {
    private final HistoryService historyService;
    private final BinanceFeignService binanceFeignService;
    private final UsersService userService;
    private final TradeService tradeService;
    private final StrategyRepository strategyRepository;
    private final AESUtils aesUtils;

    public void handleWebHook(WebHookDto request) {
        Strategy strategy = findByName(request.getName());
        String strategyName = strategy.getName();
        TradingType strategyCurrentPosition = strategy.getCurrentPosition().getTradingType();

        autoTrading(strategyName, strategyCurrentPosition).thenRun(() -> {
            closeOngoingHistory(strategy, request.getPosition());
            createNewHistory(strategy, request.getPosition());
            updateStrategyMetaData(strategy, request.getPosition());
        });
    }

    public FindStrategiesInfoResponseDto findFutureStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = findFutureStrategyInfoDaos();
        return new FindStrategiesInfoResponseDto(strategiesInfo);
    }

    public FindStrategiesInfoResponseDto findSpotStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = findSpotStrategyInfoDaos();
        return new FindStrategiesInfoResponseDto(strategiesInfo);
    }

    public void subscribeStrategy(SubscribeStrategyDto request) {
        Users savedUser = getUserFromSecurityContext();
        Strategy strategy = findById(request.getId());
        String encryptedApiKey = getEncryptedKey(request.getBinanceApiKey());
        String encryptedSecretKey = getEncryptedKey(request.getBinanceSecretKey());

        savedUser.subscribeStrategy(strategy, encryptedApiKey, encryptedSecretKey);
    }

    public void unsubscribeStrategy(UnSubscribeStrategyDto request) {
        Users savedUser = getUserFromSecurityContext();
        //TODO - Strategy strategy = savedUser.getStrategy(); 이거 되는지 테스트
        if (request.isPositionClose() && isUserPositionExist(savedUser.getCurrentPositionType())) {
            String side = getSideFromUserCurrentPosition(savedUser);
            closePosition(savedUser.getBinanceApiKey(), savedUser.getBinanceSecretKey(), side);
        }

        savedUser.unsubscribeStrategy();

    }

    private CompletableFuture<Void> autoTrading(String name, TradingType tradingType) {
        return tradeService.autoTrading(name, tradingType);
    }

    private static String getSideFromUserCurrentPosition(Users savedUser) {
        return savedUser.getCurrentPositionType().equals(TradingType.LONG) ? "SELL" : "BUY";
    }

//    public void createStrategy(CreateStrategyDto request) {
//        Strategy strategy = Strategy.of(request.getName(), request.getStrategyType(), request.getCoinType(), request.getProfitFactor(), request.getWinningRate(),
//                request.getSimpleProfitRate(), request.getCompoundProfitRate(), request.getTotalProfitRate(),
//                request.getTotalLossRate(), request.getWinCount(), request.getLossCount(), request.getCurrentPosition(), request.getAverageHoldingPeriod(), request.getAverageProfitRate());
//
//        strategyRepository.save(strategy);
//    }

    private void closePosition(String apiKey, String secretKey, String side) {
        binanceFeignService.closePosition(apiKey, secretKey, side);
    }

    private String getEncryptedKey(String key) {
        return aesUtils.encrypt(key);
    }

    private Users getUserFromSecurityContext() {
        return userService.getUserFromSecurityContext();
    }

    private Strategy findById(Long id) {
        return strategyRepository.findById(id)
                .orElseThrow(() -> new TradinException(ExceptionMessage.NOT_FOUND_STRATEGY_EXCEPTION));
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
                .orElseThrow(() -> new TradinException(ExceptionMessage.NOT_FOUND_STRATEGY_EXCEPTION));
    }

    private List<StrategyInfoDao> findFutureStrategyInfoDaos() {
        return strategyRepository.findFutureStrategiesInfoDao()
                .orElseThrow(() -> new TradinException(ExceptionMessage.NOT_FOUND_ANY_STRATEGY_EXCEPTION));
    }

    private List<StrategyInfoDao> findSpotStrategyInfoDaos() {
        return strategyRepository.findSpotStrategiesInfoDao()
                .orElseThrow(() -> new TradinException(ExceptionMessage.NOT_FOUND_ANY_STRATEGY_EXCEPTION));
    }

    private boolean isUserPositionExist(TradingType tradingType) {
        return tradingType != TradingType.NONE;
    }

}
