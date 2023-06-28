package com.traders.traders.module.users.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.UsersQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.traders.traders.module.strategy.domain.QStrategy.strategy;
import static com.traders.traders.module.users.domain.QUsers.users;

@RequiredArgsConstructor
public class UsersQueryRepositoryImpl implements UsersQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Users> findByAutoTradingSubscriber(String name) {
        return jpaQueryFactory.selectFrom(users)
                .innerJoin(users.strategy, strategy)
                .where(strategy.name.eq(name))
                .fetch();
    }
}
