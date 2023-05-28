package com.traders.traders.module.strategy.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traders.traders.module.strategy.domain.Strategy;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long>, StrategyQueryRepository {
	Optional<Strategy> findByName(String name);
}
