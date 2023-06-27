package com.traders.traders.module.strategy.controller;

import com.traders.traders.module.strategy.controller.dto.request.WebHookRequestDto;
import com.traders.traders.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.traders.traders.module.strategy.service.StrategyService;
import com.traders.traders.module.users.controller.dto.request.SubscribeStrategyRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/strategies")
@Slf4j
public class StrategyController {
    //TODO - 비동기로 성능 개선 포인트 찾아보기

    private final StrategyService strategyService;

    //TODO - 자동매매 구독자 잔뜩 생성해서 4개 전략 동시에 테스트 해보기
    //TODO - 웹 훅 발생시 매매 내역 캐시 삭제하고 다시 캐시하는 로직 추가하기
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


    //TODO - 개발용 메서드!! 추후 삭제하기
//    @PostMapping("/create")
//    public void createStrategy(@Valid @RequestBody CreateStrategyDto request) {
//        strategyService.createStrategy(request);
//    }
}
