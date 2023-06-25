package com.traders.traders.module.history.controller;

import com.traders.traders.module.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/histories")
public class HistoryController {
    private final HistoryService historyService;

    //TODO - 다른 전략이랑 한 화면에서 비교를 해줄 수도 있고!!
}
