package com.traders.traders.module.users.domain.repository;

import com.traders.traders.module.users.domain.Users;

import java.util.List;

public interface UsersQueryRepository {
    List<Users> findByAutoTradingSubscriber(String name);
}
