package com.traders.traders.module.users.domain.repository;

import java.util.List;

import com.traders.traders.module.users.domain.repository.dao.AutoTradingSubscriberDao;

public interface UsersQueryRepository {
	List<AutoTradingSubscriberDao> findByAutoTradingSubscriber(String name);
}
