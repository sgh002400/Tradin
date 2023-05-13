package com.traders.traders.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordEncoder {
	private final BCryptPasswordEncoder encoder;

	public String encode(String password) {
		return encoder.encode(password);
	}

	public boolean matches(String password, String encodedPassword) {
		return encoder.matches(password, encodedPassword);
	}
}
