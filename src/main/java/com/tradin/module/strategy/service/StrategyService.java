package com.tradin.module.strategy.service;

import com.tradin.common.exception.ExceptionMessage;
import com.tradin.common.exception.TradinException;
import com.tradin.common.utils.AESUtils;
import com.tradin.module.feign.service.FeignService;
import com.tradin.module.history.service.HistoryService;
import com.tradin.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.domain.Position;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.domain.TradingType;
import com.tradin.module.strategy.domain.repository.StrategyRepository;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.service.dto.UnSubscribeStrategyDto;
import com.tradin.module.strategy.service.dto.WebHookDto;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.service.UsersService;
import com.tradin.module.users.service.dto.SubscribeStrategyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
    private final FeignService feignService;
    private final UsersService userService;
    private final StrategyRepository strategyRepository;
    private final AESUtils aesUtils;

    public void handleWebHook(WebHookDto request) {
        Strategy strategy = findByName(request.getName());

        autoTrading(strategy).thenRun(() -> {
            closeOngoingHistory(strategy, request.getPosition());
            createNewHistory(strategy, request.getPosition());
            updateStrategyMetaData(strategy, request.getPosition());
        });
    }

    @Async
    public CompletableFuture<Void> autoTrading(Strategy strategy) {
        List<Users> autoTradingSubscribers = userService.findAutoTradingSubscriberByStrategyName(
                strategy.getName());

        for (Users autoTradingSubscriber : autoTradingSubscribers) {
            String apiKey = getDecryptedKey(autoTradingSubscriber.getBinanceApiKey());
            String secretKey = getDecryptedKey(autoTradingSubscriber.getBinanceSecretKey());

            trade(autoTradingSubscriber, strategy, apiKey, secretKey);
        }

        return CompletableFuture.completedFuture(null);
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

    private void trade(Users user, Strategy strategy, String apiKey, String secretKey) {
        TradingType currentPosition = getCurrentPosition(strategy);

        if (currentPosition == TradingType.LONG) {
            processLongPosition(apiKey, secretKey, "SELL", user);
        } else if (currentPosition == TradingType.SHORT) {
            processShortPosition(apiKey, secretKey, "BUY", user);
        }

    }

    private TradingType getCurrentPosition(Strategy strategy) {
        return strategy.getCurrentPosition().getTradingType();
    }

    private void processLongPosition(String apikey, String secretKey, String side, Users user) {
        if (isUserTradingTypeContainsShort(user)) {
            int orderQuantity = calculateOrderQuantity(apikey, secretKey, user.getLeverage(), user.getQuantityRate());

            if (isUserPositionExist(user.getCurrentPositionType())) {
                switchAndChangeCurrentPosition(apikey, secretKey, side, orderQuantity, user, TradingType.SHORT);
            } else {
                openAndChangeCurrentPosition(apikey, secretKey, side, orderQuantity, user, TradingType.SHORT);
            }
        } else {
            closeAndChangeCurrentPosition(apikey, secretKey, side, user, TradingType.NONE);
        }
    }

    private void processShortPosition(String apikey, String secretKey, String side, Users user) {
        if (isUserTradingTypeContainsLong(user)) {
            int orderQuantity = calculateOrderQuantity(apikey, secretKey, user.getLeverage(), user.getQuantityRate());

            if (isUserPositionExist(user.getCurrentPositionType())) {
                switchAndChangeCurrentPosition(apikey, secretKey, side, orderQuantity, user, TradingType.LONG);
            } else {
                openAndChangeCurrentPosition(apikey, secretKey, side, orderQuantity, user, TradingType.LONG);
            }
        } else {
            closeAndChangeCurrentPosition(apikey, secretKey, side, user, TradingType.NONE);
        }
    }

    private void switchAndChangeCurrentPosition(String apikey, String secretKey, String side, int orderQuantity, Users user, TradingType type) {
        switchPosition(apikey, secretKey, side, orderQuantity);
        changeCurrentPosition(user, type);
    }

    private void closeAndChangeCurrentPosition(String apikey, String secretKey, String side, Users user, TradingType type) {
        closePosition(apikey, secretKey, side);
        changeCurrentPosition(user, type);
    }

    private void openAndChangeCurrentPosition(String apikey, String secretKey, String side, int orderQuantity, Users user, TradingType type) {
        openPosition(apikey, secretKey, side, orderQuantity);
        changeCurrentPosition(user, type);
    }

    private void switchPosition(String apiKey, String secretKey, String side, int orderQuantity) {
        feignService.openPosition(apiKey, secretKey, side, orderQuantity);
    }

    private void closePosition(String apiKey, String secretKey, String side) {
        feignService.closePosition(apiKey, secretKey, side);
    }

    private void openPosition(String apiKey, String secretKey, String side, int orderQuantity) {
        feignService.openPosition(apiKey, secretKey, side, orderQuantity);
    }

    private static void changeCurrentPosition(Users user, TradingType tradingType) {
        user.changeCurrentPosition(tradingType);
    }

    private int calculateOrderQuantity(String apiKey, String secretKey, int leverage, int quantityRate) {
        int futureAccountBalance = feignService.getFutureAccountBalance(apiKey, secretKey);
        return futureAccountBalance * leverage * quantityRate / 100;
    }

    private boolean isUserPositionExist(TradingType tradingType) {
        return tradingType != TradingType.NONE;
    }

    private boolean isUserTradingTypeContainsLong(Users user) {
        return user.getTradingType() == TradingType.LONG || user.getTradingType() == TradingType.BOTH;
    }

    private boolean isUserTradingTypeContainsShort(Users user) {
        return user.getTradingType() == TradingType.SHORT || user.getTradingType() == TradingType.BOTH;
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
}
