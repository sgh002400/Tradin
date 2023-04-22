package com.traders.traders.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public BaseException(ExceptionMessage exceptionMessage) {
		this.httpStatus = exceptionMessage.getHttpStatus();
		this.errorCode = exceptionMessage.toString();
		this.message = exceptionMessage.getMessage();
	}
}