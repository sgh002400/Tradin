package com.tradin.module.auth.controller;

import com.tradin.module.auth.service.AuthService;
import com.tradin.module.users.controller.dto.response.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "인증 - 구글 로그인&회원가입")
    @PostMapping("/auth/cognito")
    public ResponseEntity<TokenResponseDto> auth(@RequestParam String code) {
        return ResponseEntity.ok(authService.auth(code));
    }
}
