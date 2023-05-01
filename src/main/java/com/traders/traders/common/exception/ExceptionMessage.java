package com.traders.traders.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
	//400 Bad Request
	NOT_FOUND_USER_EXCEPTION(NOT_FOUND, "존재하지 않는 유저입니다."),

	//401 Unauthorized

	//403 Forbidden

	//404 Not Found

	//405 Method Not Allowed

	//409 Conflict

	//429 Too Many Requests
	IP_RATE_LIMIT_EXCEEDED_EXCEPTION(TOO_MANY_REQUESTS, "IP당 최대 요청 횟수를 초과하였습니다."),

	//500 Internal Server Error
	;

	private final HttpStatus httpStatus;
	private final String message;

	ExceptionMessage(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
