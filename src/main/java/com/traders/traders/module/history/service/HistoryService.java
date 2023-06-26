package com.traders.traders.module.history.service;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.history.controller.dto.response.BackTestResponseDto;
import com.traders.traders.module.history.domain.History;
import com.traders.traders.module.history.domain.repository.HistoryRepository;
import com.traders.traders.module.history.domain.repository.dao.HistoryDao;
import com.traders.traders.module.history.service.dto.BackTestDto;
import com.traders.traders.module.history.service.dto.StrategyInfoDto;
import com.traders.traders.module.strategy.domain.Position;
import com.traders.traders.module.strategy.domain.Strategy;
import com.traders.traders.module.strategy.domain.TradingType;
import com.traders.traders.module.strategy.service.dto.HistoryCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.traders.traders.common.exception.ExceptionMessage.NOT_FOUND_OPEN_POSITION_EXCEPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void closeOngoingHistory(Strategy strategy, Position position) {
        History history = findLastHistoryByStrategyId(strategy.getId());
        closeOpenPosition(history, position);
        calculateProfitRate(history); //TODO - history.exitPosition 잘 되는지랑 수치 계산 잘 되는지 테스트 해보기
    }

    public void createNewHistory(Strategy strategy, Position position) {
        History newHistory = History.of(position, strategy);
        historyRepository.save(newHistory);
    }

    public List<HistoryDao> findHistoryDaoByStrategyId(Long id) {
        return historyRepository.findHistoryDaoByStrategyId(id);
    }

    //TODO - 비동기 처리
    public BackTestResponseDto backTest(BackTestDto request) {
        String cacheKey = "strategyId:" + request.getId();

        //캐시에 매매 내역들 있는지 확인
        HistoryCache historyCache = (HistoryCache) redisTemplate.opsForValue().get(cacheKey);

        //없다면 매매 내역 전체 캐싱
        if (historyCache == null) {
            List<HistoryDao> histories = findHistoryDaoByStrategyId(request.getId());
            historyCache = HistoryCache.of(histories);
            redisTemplate.opsForValue().set(cacheKey, historyCache);
        }

        //매매 내역들을 조건에 맞게 계산 후 응답
        return calculateHistoryCache(historyCache, request);
    }

    //TODO - 누적 수익률 계산하기
    private BackTestResponseDto calculateHistoryCache(HistoryCache historyCache, BackTestDto request) {
        List<HistoryDao> histories = new ArrayList<>();
        double compoundProfitRate = 1;
        double winCount = 0;
        int totalTradeCount = 0;
        double simpleProfitRate = 0;
        double winProfitRate = 0;
        double loseProfitRate = 0;

        for (HistoryDao history : historyCache.getHistories()) {
            if (!isInPeriod(history, request.getStartDate(), request.getEndDate())) continue;
            if (!isCorrespondTradingType(history, request.getTradingTypes())) continue;

            totalTradeCount++;
            compoundProfitRate = compoundProfitRate * (1 + history.getProfitRate());
            history.setCompoundProfitRate(compoundProfitRate);
            histories.add(history);

            double profitRate = history.getProfitRate();
            if (profitRate > 0) {
                winProfitRate += profitRate;
                winCount++;
            } else if (profitRate < 0) {
                loseProfitRate += Math.abs(profitRate);
            }

            simpleProfitRate += profitRate;
        }

        Collections.reverse(histories);

        double winRate = calculateWinRate(winCount, totalTradeCount);
        double averageProfitRate = calculateAverageProfitRate(simpleProfitRate, totalTradeCount);
        double profitFactor = calculateProfitFactor(winProfitRate, loseProfitRate);

        StrategyInfoDto strategyInfoDto = StrategyInfoDto.builder()
                .id(request.getId())
                .name(request.getName())
                .compoundProfitRate(compoundProfitRate)
                .winRate(winRate)
                .profitFactor(profitFactor)
                .totalTradeCount(totalTradeCount)
                .averageProfitRate(averageProfitRate)
                .build();

        return BackTestResponseDto.of(strategyInfoDto, histories);
    }

    private double calculateWinRate(double winCount, int totalTradeCount) {
        return totalTradeCount == 0 ? 0 : winCount / totalTradeCount;
    }

    private double calculateAverageProfitRate(double simpleProfitRate, int totalTradeCount) {
        return totalTradeCount == 0 ? 0 : simpleProfitRate / totalTradeCount;
    }

    private double calculateProfitFactor(double winProfitRate, double loseProfitRate) {
        return loseProfitRate == 0 ? 0 : winProfitRate / loseProfitRate;
    }


    private boolean isInPeriod(HistoryDao history, LocalDateTime startDate, LocalDateTime endDate) {
        return history.getEntryPosition().getTime().isAfter(startDate) &&
                history.getExitPosition().getTime().isBefore(endDate);
    }

    private boolean isCorrespondTradingType(HistoryDao historyDao, List<TradingType> tradingTypes) {
        return tradingTypes.contains(historyDao.getEntryPosition().getTradingType());
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
