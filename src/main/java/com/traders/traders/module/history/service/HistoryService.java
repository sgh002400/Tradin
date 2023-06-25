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
import com.traders.traders.module.strategy.service.dto.HistoryCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private BackTestResponseDto calculateHistoryCache(HistoryCache historyCache, BackTestDto request) {
        double compoundProfitRate = 0;
        double winCount = 0;
        double totalCount = 0; //TODO - 기간을 이상하게 설정해서 매매 내역이 없는 경우 0으로 나누게 됐을 때 예외 발생시키기
        double totalProfitRate = 0;
        double totalLossRate = 0;
        List<HistoryDao> histories = new ArrayList<>();
        //TODO - 숏 총 순익, 롱 총 순익 추가하기

        for (HistoryDao history : historyCache.getHistories()) {
            if (isInPeriod(history, request.getStartDate(), request.getEndDate())) {
                //TODO - 롱, 숏, 롱숏 if문 처리
                totalCount++;
                compoundProfitRate = compoundProfitRate * (1 + history.getProfitRate());
                histories.add(history);

                if (history.getProfitRate() > 0) {
                    winCount++;
                    totalProfitRate += history.getProfitRate();
                } else {
                    totalLossRate += history.getProfitRate();
                }
            }
        }

        StrategyInfoDto strategyInfoDto = StrategyInfoDto.of(request.getId(), request.getName(), compoundProfitRate, winCount / totalCount,
                totalProfitRate / totalLossRate);

        return BackTestResponseDto.of(strategyInfoDto, histories);

    }

    private boolean isInPeriod(HistoryDao history, LocalDateTime startDate, LocalDateTime endDate) {
        return history.getEntryPosition().getTime().isAfter(startDate) &&
                history.getExitPosition().getTime().isBefore(endDate);
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
