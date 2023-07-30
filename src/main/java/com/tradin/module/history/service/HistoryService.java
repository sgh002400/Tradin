package com.tradin.module.history.service;

import com.tradin.common.exception.TradinException;
import com.tradin.module.history.controller.dto.response.BackTestResponseDto;
import com.tradin.module.history.domain.History;
import com.tradin.module.history.domain.repository.HistoryRepository;
import com.tradin.module.history.domain.repository.dao.HistoryDao;
import com.tradin.module.history.service.dto.BackTestDto;
import com.tradin.module.history.service.dto.StrategyInfoDto;
import com.tradin.module.strategy.domain.Position;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.domain.TradingType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.tradin.common.exception.ExceptionMessage.NOT_FOUND_OPEN_POSITION_EXCEPTION;
import static com.tradin.common.exception.ExceptionMessage.NOT_FOUND_STRATEGY_EXCEPTION;
import static com.tradin.module.strategy.domain.TradingType.BOTH;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final RedisTemplate<String, HistoryDao> historyRedisTemplate;

    public void closeOngoingHistory(Strategy strategy, Position exitPosition) {
        History ongoingHistory = findLastHistoryByStrategyId(strategy.getId());
        closeOpenPosition(ongoingHistory, exitPosition);
        calculateProfitRate(ongoingHistory);
    }

    public void createNewHistory(Strategy strategy, Position position) {
        History newHistory = History.of(position, strategy);
        historyRepository.save(newHistory);
    }

    public List<HistoryDao> findHistoryDaoByStrategyId(Long id) {
        return historyRepository.findHistoryDaoByStrategyId(id);
    }

    public BackTestResponseDto backTest(BackTestDto request) {
        List<HistoryDao> historyDaos = getHistories(request.getId(), request.getStartDate(), request.getEndDate());

        return calculateHistoryDaos(historyDaos, request);
    }

    public List<HistoryDao> getHistories(Long strategyId, LocalDate startDate, LocalDate endDate) {
        String cacheKey = "strategyId:" + strategyId;

        ZSetOperations<String, HistoryDao> ops = historyRedisTemplate.opsForZSet();

        LocalDateTime startTime = startDate.atStartOfDay().minusHours(9);
        LocalDateTime endTime = endDate.atStartOfDay().minusHours(9).minusDays(1);

        long startScore = startTime.toEpochSecond(ZoneOffset.UTC);
        long endScore = endTime.toEpochSecond(ZoneOffset.UTC);

        Set<HistoryDao> historySet = ops.rangeByScore(cacheKey, startScore, endScore);

        if (historySet != null && historySet.isEmpty()) {
            addHistory(cacheKey, strategyId);
            historySet = ops.rangeByScore(cacheKey, startScore, endScore);
        }

        if (historySet != null && historySet.isEmpty()) {
            throw new TradinException(NOT_FOUND_STRATEGY_EXCEPTION);
        }

        return new ArrayList<>(historySet);
    }


    public void addHistory(String cacheKey, Long strategyId) {
        List<HistoryDao> historyDaos = findHistoryDaoByStrategyId(strategyId);
        ZSetOperations<String, HistoryDao> ops = historyRedisTemplate.opsForZSet();

        for (HistoryDao historyDao : historyDaos) {
            ops.add(cacheKey, historyDao, historyDao.getEntryPosition().getTime().toEpochSecond(ZoneOffset.UTC));
        }
    }


    private BackTestResponseDto calculateHistoryDaos(List<HistoryDao> historyDaos, BackTestDto request) {
        List<HistoryDao> histories = new ArrayList<>();
        double compoundProfitRate = 1;
        double winCount = 0;
        int totalTradeCount = 0;
        double simpleProfitRate = 0;
        double winProfitRate = 0;
        double loseProfitRate = 0;

        for (HistoryDao history : historyDaos) {
            if (isInPeriod(history, request.getStartDate(), request.getEndDate()) &&
                    isCorrespondTradingType(history, request.getTradingType())) {

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
        }

        Collections.reverse(histories);

        double winRate = calculateWinRate(winCount, totalTradeCount);
        double averageProfitRate = calculateAverageProfitRate(simpleProfitRate, totalTradeCount);
        double profitFactor = calculateProfitFactor(winProfitRate, loseProfitRate);

        StrategyInfoDto strategyInfoDto = StrategyInfoDto.of(request.getId(), request.getName(), compoundProfitRate, winRate, profitFactor, totalTradeCount, averageProfitRate);

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


    private boolean isInPeriod(HistoryDao history, LocalDate startDate, LocalDate endDate) {
        return history.getEntryPosition().getTime().isAfter(startDate.atStartOfDay()) &&
                history.getExitPosition().getTime().isBefore(endDate.atStartOfDay());
    }

    private boolean isCorrespondTradingType(HistoryDao historyDao, TradingType tradingType) {
        return tradingType == historyDao.getEntryPosition().getTradingType() || tradingType == BOTH;
    }

    private static void calculateProfitRate(History history) {
        history.calculateProfitRate();
    }

    private static void closeOpenPosition(History ongoingHistory, Position exitPosition) {
        if (ongoingHistory.getExitPosition() != null) {
            throw new TradinException(NOT_FOUND_OPEN_POSITION_EXCEPTION);

        }
        ongoingHistory.closeOpenPosition(exitPosition);
    }

    private History findLastHistoryByStrategyId(Long id) {
        return historyRepository.findLastHistoryByStrategyId(id)
                .orElseThrow(() -> new TradinException(NOT_FOUND_OPEN_POSITION_EXCEPTION));
    }
}
