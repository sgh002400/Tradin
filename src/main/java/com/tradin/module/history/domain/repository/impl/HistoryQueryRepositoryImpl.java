package com.tradin.module.history.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.history.domain.QHistory;
import com.tradin.module.history.domain.repository.HistoryQueryRepository;
import com.tradin.module.history.domain.repository.dao.HistoryDao;
import com.tradin.module.history.domain.repository.dao.QHistoryDao;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class HistoryQueryRepositoryImpl implements HistoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HistoryDao> findHistoryDaoByStrategyId(Long id) {
        return jpaQueryFactory.select(new QHistoryDao(QHistory.history.id, QHistory.history.entryPosition, QHistory.history.exitPosition,
                        QHistory.history.profitRate))
                .from(QHistory.history)
                .where(QHistory.history.strategy.id.eq(id))
                .orderBy(QHistory.history.id.asc())
                .fetch();
    }
}
