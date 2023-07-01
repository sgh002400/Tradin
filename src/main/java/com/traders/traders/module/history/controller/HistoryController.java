package com.traders.traders.module.history.controller;

import com.traders.traders.module.history.controller.dto.request.BackTestRequestDto;
import com.traders.traders.module.history.controller.dto.response.BackTestResponseDto;
import com.traders.traders.module.history.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/histories")
public class HistoryController {
    private final HistoryService historyService;

    @Operation(summary = "백테스트 탭 - 백테스트")
    @GetMapping("")
    public BackTestResponseDto backTest(@Valid @ModelAttribute BackTestRequestDto request) {
        return historyService.backTest(request.toServiceDto());
    }
}
