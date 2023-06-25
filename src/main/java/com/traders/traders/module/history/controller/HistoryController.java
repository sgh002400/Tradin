package com.traders.traders.module.history.controller;

import com.traders.traders.module.history.controller.dto.request.BackTestRequestDto;
import com.traders.traders.module.history.controller.dto.response.BackTestResponseDto;
import com.traders.traders.module.history.service.HistoryService;
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

    @GetMapping("")
    public BackTestResponseDto backTest(@Valid @ModelAttribute BackTestRequestDto request) {
        return historyService.backTest(request.toServiceDto());
    }
}
