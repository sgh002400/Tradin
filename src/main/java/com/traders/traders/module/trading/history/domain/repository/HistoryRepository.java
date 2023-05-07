package com.traders.traders.module.trading.history.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traders.traders.module.trading.history.domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
	Optional<History> findLastHistoryByStrategyId(Long id);
}
