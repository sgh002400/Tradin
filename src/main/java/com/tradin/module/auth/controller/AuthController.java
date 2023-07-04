package com.tradin.module.auth.controller;

import com.tradin.module.auth.controller.dto.request.TokenRequestDto;
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

    @Operation(summary = "인증 탭 - 구글 로그인&회원가입")
    @PostMapping("/auth/cognito")
    public ResponseEntity<String> googleAuth(@RequestParam String code) {
        authService.requestTokenToCognito(code);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "Cognito Redirect URL")
    @PostMapping("/auth/token")
    public ResponseEntity<TokenResponseDto> cognitoRedirect(@Valid @RequestBody TokenRequestDto request) {
        return ResponseEntity.ok(authService.auth(request.toServiceDto()));
    }
}
