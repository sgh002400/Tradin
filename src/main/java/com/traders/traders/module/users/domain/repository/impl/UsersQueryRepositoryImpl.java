package com.traders.traders.module.users.domain.repository.impl;

import static com.traders.traders.module.strategy.domain.QStrategy.*;
import static com.traders.traders.module.users.domain.QUsers.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.traders.traders.module.users.domain.repository.UsersQueryRepository;
import com.traders.traders.module.users.domain.repository.dao.AutoTradingSubscriberDao;
import com.traders.traders.module.users.domain.repository.dao.QAutoTradingSubscriberDao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsersQueryRepositoryImpl implements UsersQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<AutoTradingSubscriberDao> findByAutoTradingSubscriber(String name) {
		return jpaQueryFactory.select(new QAutoTradingSubscriberDao(
				users.leverage,
				users.quantity,
				users.binanceApiKey,
				users.binanceSecretKey
			))
			.from(users)
			.join(users.strategy, strategy)
			.where(users.name.eq(name))
			.fetch();
	}
}
