package com.traders.traders.module.users.controller.dto.request;

import javax.validation.constraints.NotBlank;

import com.traders.traders.module.users.service.dto.SignUpDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignUpRequestDto {
	@NotBlank(message = "Email must not be blank")
	private String email;

	@NotBlank(message = "Password must not be blank")
	private String password;

	public SignUpDto toServiceDto() {
		return SignUpDto.of(email, password);
	}
}
