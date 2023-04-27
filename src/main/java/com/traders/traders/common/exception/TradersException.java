package com.traders.traders.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TradersException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final String message;

	public TradersException(ExceptionMessage exceptionMessage) {
		this.httpStatus = exceptionMessage.getHttpStatus();
		this.message = exceptionMessage.getMessage();
	}
}