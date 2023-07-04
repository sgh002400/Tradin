package com.tradin.module.auth.controller;

import com.tradin.module.auth.controller.dto.request.TokenReissueRequestDto;
import com.tradin.module.auth.service.AuthService;
import com.tradin.module.users.controller.dto.response.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @Operation(summary = "인증 - 엑세스 토큰 재발급")
    public ResponseEntity<String> reissueToken(@Valid @RequestBody TokenReissueRequestDto request) {
        return ResponseEntity.ok(authService.reissueToken(request.toServiceDto());
    }
}
