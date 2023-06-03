package com.traders.traders.module.users.domain.repository;

import java.util.List;
import java.util.Optional;

import com.traders.traders.module.users.domain.SocialInfo;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.dao.AutoTradingSubscriberDao;

public interface UsersQueryRepository {
	List<AutoTradingSubscriberDao> findByAutoTradingSubscriber(String name);

	Optional<Users> findBySocialInfo(SocialInfo socialInfo);
}
