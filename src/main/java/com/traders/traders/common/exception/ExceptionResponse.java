package com.traders.traders.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponse {

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public static ExceptionResponse of(HttpStatus httpStatus, String errorCode, String message) {
		return ExceptionResponse.builder()
			.httpStatus(httpStatus)
			.errorCode(errorCode)
			.message(message)
			.build();
	}
}