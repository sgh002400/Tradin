package com.traders.traders.module.strategy.controller;

import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traders.traders.module.strategy.controller.dto.request.BackTestRequestDto;
import com.traders.traders.module.strategy.controller.dto.request.CreateStrategyDto;
import com.traders.traders.module.strategy.controller.dto.request.WebHookRequestDto;
import com.traders.traders.module.strategy.controller.dto.response.BackTestResponseDto;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.service.StrategyService;
import com.traders.traders.module.users.controller.dto.request.SubscribeStrategyRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/strategies")
@Slf4j
public class StrategyController {
	private final StrategyService strategyService;

	//TODO - 자동매매 구독자 잔뜩 생성해서 4개 전략 동시에 테스트 해보기
	@KafkaListener(topics = "future-short-term-v1", groupId = "trading-strategy-executors")
	public void handleFutureShortTermV1WebHook(@RequestBody WebHookRequestDto request) {
		CompletableFuture.runAsync(() -> strategyService.handleWebHook(request.toServiceDto()));
	}

	@KafkaListener(topics = "future-long-term-v1", groupId = "trading-strategy-executors")
	public void handleFutureLongTermV1WebHook(@RequestBody WebHookRequestDto request) {
		CompletableFuture.runAsync(() -> strategyService.handleWebHook(request.toServiceDto()));
	}

	@KafkaListener(topics = "spot-short-term-v1", groupId = "trading-strategy-executors")
	public void handleSpotShortTermV1WebHook(@RequestBody WebHookRequestDto request) {
		CompletableFuture.runAsync(() -> strategyService.handleWebHook(request.toServiceDto()));
	}

	@KafkaListener(topics = "spot-long-term-v1", groupId = "trading-strategy-executors")
	public void handleSpotLongTermV1WebHook(@RequestBody WebHookRequestDto request) {
		CompletableFuture.runAsync(() -> strategyService.handleWebHook(request.toServiceDto()));
	}

	@GetMapping()
	public FindStrategiesInfoResponseDto findStrategiesInfos() {
		return strategyService.findStrategiesInfo();
	}

	@PostMapping("/{id}/subscriptions")
	public void subscribe(@Valid @RequestBody SubscribeStrategyRequestDto request, @PathVariable Long id) {
		strategyService.subscribeStrategy(request.toServiceDto(id));
	}

	@GetMapping("/back-test")
	public BackTestResponseDto backTest(@Valid @ModelAttribute BackTestRequestDto request) {
		return strategyService.backTest(request.toServiceDto());
	}

	//TODO - 개발용 메서드!! 추후 삭제하기
	@PostMapping("/create")
	public void createStrategy(@Valid @RequestBody CreateStrategyDto request) {
		strategyService.createStrategy(request);
	}
}
