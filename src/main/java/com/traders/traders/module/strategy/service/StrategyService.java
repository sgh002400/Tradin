package com.traders.traders.module.strategy.service;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.common.utils.AESUtils;
import com.traders.traders.module.feign.service.FeignService;
import com.traders.traders.module.history.domain.repository.dao.HistoryDao;
import com.traders.traders.module.history.service.HistoryService;
import com.traders.traders.module.strategy.controller.dto.request.CreateStrategyDto;
import com.traders.traders.module.strategy.controller.dto.response.BackTestResponseDto;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.domain.Position;
import com.traders.traders.module.strategy.domain.Strategy;
import com.traders.traders.module.strategy.domain.repository.StrategyRepository;
import com.traders.traders.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.traders.traders.module.strategy.service.dto.*;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.dao.AutoTradingSubscriberDao;
import com.traders.traders.module.users.service.UsersService;
import com.traders.traders.module.users.service.dto.SubscribeStrategyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.traders.traders.common.exception.ExceptionMessage.NOT_FOUND_ANY_STRATEGY_EXCEPTION;
import static com.traders.traders.common.exception.ExceptionMessage.NOT_FOUND_STRATEGY_EXCEPTION;

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
    private final RedisTemplate<String, Object> redisTemplate;

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

        List<StrategyHistoryDto> strategyHistoryDtos = new ArrayList<>();

        for (Long id : request.getIds()) {
            String cacheKey = "strategyId:" + id;

            //캐시에 매매 내역들 있는지 확인
            HistoryCache historyCache = (HistoryCache) redisTemplate.opsForValue().get(cacheKey);

            //없다면 매매 내역 전체 캐싱
            if (historyCache == null) {
                List<HistoryDao> histories = findHistoryDaoByStrategyId(id);
                historyCache = HistoryCache.of(histories);
                redisTemplate.opsForValue().set(cacheKey, historyCache);
            }

            //매매 내역들을 조건에 맞게 (기간, 자기 자본 100) 계산 후 응답
            strategyHistoryDtos.add(calculateHistoryCache(historyCache, request, id));
        }

        return BackTestResponseDto.of(strategyHistoryDtos);
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

    private List<HistoryDao> findHistoryDaoByStrategyId(Long id) {
        return historyService.findHistoryDaoByStrategyId(id);
    }

    private StrategyHistoryDto calculateHistoryCache(HistoryCache historyCache, BackTestDto request,
                                                     Long strategyId) {
        Long id = strategyId;
        double compoundProfitRate = 0;
        double winCount = 0;
        double totalCount = 0; //TODO - 기간을 이상하게 설정해서 매매 내역이 없는 경우 0으로 나누게 됐을 때 예외 발생시키기
        double totalProfitRate = 0;
        double totalLossRate = 0;
        //TODO - 숏 총 순익, 롱 총 순익 추가하기

        for (HistoryDao history : historyCache.getHistories()) {
            if (isInPeriod(history, request.getStartDate(), request.getEndDate())) {
                totalCount++;
                compoundProfitRate = compoundProfitRate * (1 + history.getProfitRate());

                if (history.getProfitRate() > 0) {
                    winCount++;
                    totalProfitRate += history.getProfitRate();
                } else {
                    totalLossRate += history.getProfitRate();
                }
            }
        }

        StrategyInfoDto strategyInfoDto = StrategyInfoDto.of(id, compoundProfitRate, winCount / totalCount,
                totalProfitRate / totalLossRate);

        return StrategyHistoryDto.of(strategyInfoDto, historyCache.getHistories());

    }

    private boolean isInPeriod(HistoryDao history, LocalDateTime startDate, LocalDateTime endDate) {
        return history.getEntryPosition().getTime().isAfter(startDate) &&
                history.getExitPosition().getTime().isBefore(endDate);
    }
}
