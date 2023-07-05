package com.tradin.module.strategy.controller;

import com.tradin.module.strategy.controller.dto.request.SubscribeStrategyRequestDto;
import com.tradin.module.strategy.controller.dto.request.UnSubscribeStrategyRequestDto;
import com.tradin.module.strategy.controller.dto.request.WebHookRequestDto;
import com.tradin.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.service.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/strategies")
@Slf4j
public class StrategyController {
    private final StrategyService strategyService;

//    //TODO - 테스트용 메서드! 추후 삭제 예
//    @KafkaListener(topics = "tradin", groupId = "trading-strategy-executors")
//    public void test(@RequestBody WebHookRequestDto request) {
//        log.info("strategyName: " + request.getName());
//        log.info("tradingType: " + request.getPosition().getTradingType());
//        log.info("tradingPrice: " + request.getPosition().getPrice());
//        log.info("time: " + request.getPosition().getTime());
//    }

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

    @Operation(summary = "전략 탭 - 선물 전략 전체 조회")
    @GetMapping("/future")
    public FindStrategiesInfoResponseDto findFutureStrategiesInfos() {
        return strategyService.findFutureStrategiesInfo();
    }

    @Operation(summary = "전략 탭 - 현물 전략 전체 조회")
    @GetMapping("/spot")
    public FindStrategiesInfoResponseDto findSpotStrategiesInfos() {
        return strategyService.findSpotStrategiesInfo();
    }

    @Operation(summary = "[인증] 자동매매 탭 - 선물 전략 구독")
    @PostMapping("/{id}/subscriptions")
    public void subscribe(@Valid @RequestBody SubscribeStrategyRequestDto request, @PathVariable Long id) {
        strategyService.subscribeStrategy(request.toServiceDto(id));
    }

    @Operation(summary = "[인증] 전략 탭 - 선물 전략 구독 취소")
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
