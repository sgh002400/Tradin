package com.traders.traders.module.history.domain.repository.impl;

import static com.traders.traders.module.history.domain.QHistory.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.traders.traders.module.history.domain.History;
import com.traders.traders.module.history.domain.repository.HistoryQueryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HistoryQueryRepositoryImpl implements HistoryQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<History> findHistoriesByStrategyName(String name) {
		return jpaQueryFactory
			.selectFrom(history)
			.innerJoin(history.strategy)
			.where(history.strategy.name.eq(name))
			.orderBy(history.id.asc())
			.fetch();
	}
}
