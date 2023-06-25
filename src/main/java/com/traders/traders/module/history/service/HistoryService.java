package com.traders.traders.module.history.service;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.history.domain.History;
import com.traders.traders.module.history.domain.repository.HistoryRepository;
import com.traders.traders.module.history.domain.repository.dao.HistoryDao;
import com.traders.traders.module.strategy.domain.Position;
import com.traders.traders.module.strategy.domain.Strategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.traders.traders.common.exception.ExceptionMessage.NOT_FOUND_OPEN_POSITION_EXCEPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

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
