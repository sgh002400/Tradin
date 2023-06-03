package com.traders.traders.module.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.traders.traders.module.users.controller.dto.SignInResponseDto;
import com.traders.traders.module.users.service.UsersService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/users")

public class UsersController {
	private final UsersService usersService;

	@PostMapping("/signin/kakao")
	public ResponseEntity<SignInResponseDto> signInWithKakao(@RequestParam("code") String code) {
		return ResponseEntity.ok(usersService.signInWithKakao(code));
	}
}
