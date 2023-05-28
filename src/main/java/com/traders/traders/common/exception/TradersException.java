package com.traders.traders.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TradersException extends RuntimeException {

	private final HttpStatus httpStatus;

	public TradersException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage.getMessage());
		this.httpStatus = exceptionMessage.getHttpStatus();
	}
}