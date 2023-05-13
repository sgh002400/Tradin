package com.traders.traders.module.users.controller.dto.request;

import javax.validation.constraints.NotBlank;

import com.traders.traders.module.users.service.dto.SignInDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignInRequestDto {
	@NotBlank(message = "Email must not be blank")
	private String email;

	@NotBlank(message = "Password must not be blank")
	private String password;

	public SignInDto toServiceDto() {
		return SignInDto.of(email, password);
	}
}
