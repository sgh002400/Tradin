package com.traders.traders.module.strategy.controller;

import com.traders.traders.module.strategy.controller.dto.request.SubscribeStrategyRequestDto;
import com.traders.traders.module.strategy.controller.dto.request.UnSubscribeStrategyRequestDto;
import com.traders.traders.module.strategy.controller.dto.request.WebHookRequestDto;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.service.StrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/strategies")
@Slf4j
public class StrategyController {
    private final StrategyService strategyService;

    @KafkaListener(topics = "future-short-term-v1", groupId = "trading-strategy-executors")
    public void handleFutureShortTermV1WebHook(@RequestBody WebHookRequestDto request) {
        strategyService.handleWebHook(request.toServiceDto());
    }

    @KafkaListener(topics = "future-long-term-v1", groupId = "trading-strategy-executors")
    public void handleFutureLongTermV1WebHook(@RequestBody WebHookRequestDto request) {
        strategyService.handleWebHook(request.toServiceDto());
    }

    @KafkaListener(topics = "spot-short-term-v1", groupId = "trading-strategy-executors")
    public void handleSpotShortTermV1WebHook(@RequestBody WebHookRequestDto request) {
        strategyService.handleWebHook(request.toServiceDto());
    }

    @KafkaListener(topics = "spot-long-term-v1", groupId = "trading-strategy-executors")
    public void handleSpotLongTermV1WebHook(@RequestBody WebHookRequestDto request) {
        strategyService.handleWebHook(request.toServiceDto());
    }

    @GetMapping("/future")
    public FindStrategiesInfoResponseDto findFutureStrategiesInfos() {
        return strategyService.findFutureStrategiesInfo();
    }

    @GetMapping("/spot")
    public FindStrategiesInfoResponseDto findSpotStrategiesInfos() {
        return strategyService.findSpotStrategiesInfo();
    }

    @PostMapping("/{id}/subscriptions")
    public void subscribe(@Valid @RequestBody SubscribeStrategyRequestDto request, @PathVariable Long id) {
        strategyService.subscribeStrategy(request.toServiceDto(id));
    }

    @PatchMapping("/unsubscriptions")
    public void unsubscribe(@Valid @RequestBody UnSubscribeStrategyRequestDto request) {
        strategyService.unsubscribeStrategy(request.toServiceDto());
    }

    //TODO - 개발용 메서드!! 추후 삭제하기
//    @PostMapping("/create")
//    public void createStrategy(@Valid @RequestBody CreateStrategyDto request) {
//        strategyService.createStrategy(request);
//    }
}
