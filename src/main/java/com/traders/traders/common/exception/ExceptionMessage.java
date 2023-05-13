package com.traders.traders.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
	//400 Bad Request

	//401 Unauthorized
	EMPTY_HEADER_EXCEPTION(UNAUTHORIZED, "헤더가 비어있습니다."),
	NOT_FOUND_REFRESH_TOKEN_EXCEPTION(UNAUTHORIZED, "존재하지 않는 리프레시 토큰입니다."),
	DIFFERENT_REFRESH_TOKEN_EXCEPTION(UNAUTHORIZED, "DB의 리프레시 토큰과 일치하지 않습니다."),
	INVALID_BEARER_FORMAT_EXCEPTION(UNAUTHORIZED, "Bearer 토큰의 형식이 올바르지 않습니다."),
	INVALID_JWT_TOKEN_EXCEPTION(UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
	NOT_FOUND_JWT_USERID_EXCEPTION(UNAUTHORIZED, "JWT 토큰에 유저 아이디가 존재하지 않습니다."),
	WRONG_PASSWORD_EXCEPTION(UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	EMAIL_ALREADY_EXISTS_EXCEPTION(UNAUTHORIZED, "이미 존재하는 이메일입니다."),

	//403 Forbidden

	//404 Not Found
	NOT_FOUND_USER_EXCEPTION(NOT_FOUND, "존재하지 않는 유저입니다."),

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
