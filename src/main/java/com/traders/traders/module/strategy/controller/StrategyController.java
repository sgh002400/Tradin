package com.traders.traders.module.strategy.controller;

import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traders.traders.module.strategy.controller.dto.request.WebHookRequestDto;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.service.StrategyService;
import com.traders.traders.module.users.controller.dto.request.SubscribeStrategyRequestDto;
import com.traders.traders.module.users.domain.Users;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/strategies")
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

	@PostMapping("/{id}/subscriptions")
	public void subScribe(@AuthenticationPrincipal Users user, @Valid @RequestBody SubscribeStrategyRequestDto request,
		@PathVariable Long id) {
		strategyService.subscribeStrategy(user, request.toServiceDto(id));
	}z
}
