package com.traders.traders.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
	;

	private final HttpStatus httpStatus;
	private final String message;

	ExceptionMessage(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
