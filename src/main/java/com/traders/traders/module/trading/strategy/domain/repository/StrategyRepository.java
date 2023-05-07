package com.traders.traders.module.trading.strategy.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traders.traders.module.trading.strategy.domain.Strategy;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {
	Optional<Strategy> findByName(String name);
}
