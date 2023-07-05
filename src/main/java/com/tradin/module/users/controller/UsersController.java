package com.tradin.module.users.controller;

import com.tradin.module.users.controller.dto.request.ChangeMetadataRequestDto;
import com.tradin.module.users.controller.dto.request.PingRequestDto;
import com.tradin.module.users.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/users")

public class UsersController {
    private final UsersService usersService;

    @Operation(summary = "[인증] 자동매매 탭 - Api&Secret Key 유효성 검사")
    @PostMapping("/binance/ping")
    public ResponseEntity<String> ping(@Valid @RequestBody PingRequestDto request) {
        return ResponseEntity.ok(usersService.ping(request.toServiceDto()));
    }

    @Operation(summary = "[인증] 자동매매 탭 - 레버리지, 수량, 포지션 타입 변경")
    @PostMapping("/binance/metadata")
    public ResponseEntity<String> changeMetaData(@Valid @RequestBody ChangeMetadataRequestDto request) {
        return ResponseEntity.ok(usersService.changeMetaData(request.toServiceDto()));
    }
}
