package com.traders.traders.module.trading.strategy.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traders.traders.module.trading.strategy.controller.dto.request.WebHookRequestDto;
import com.traders.traders.module.trading.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.trading.strategy.service.StrategyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trading/strategies")
public class StrategyController {
	private final StrategyService strategyService;

	@KafkaListener(topics = "Trading", groupId = "trading-strategy-executors")
	public void handleWebHook(@RequestBody WebHookRequestDto request) {
		CompletableFuture.runAsync(() -> strategyService.handleWebHook(request.toServiceDto()));
	}

	@GetMapping()
	public FindStrategiesInfoResponseDto findStrategiesInfos() {
		return strategyService.findStrategiesInfo();
	}
}
