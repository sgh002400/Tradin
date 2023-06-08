package com.traders.traders.module.history.domain.repository.impl;

import static com.traders.traders.module.history.domain.QHistory.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.traders.traders.module.history.domain.repository.HistoryQueryRepository;
import com.traders.traders.module.history.domain.repository.dao.HistoryDao;
import com.traders.traders.module.history.domain.repository.dao.QHistoryDao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HistoryQueryRepositoryImpl implements HistoryQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<HistoryDao> findHistoryDaoByStrategyName(String name) {
		return jpaQueryFactory.select(new QHistoryDao(history.id, history.entryPosition, history.exitPosition,
				history.profitRate))
			.from(history)
			.where(history.strategy.name.eq(name))
			.fetch();
	}
}
