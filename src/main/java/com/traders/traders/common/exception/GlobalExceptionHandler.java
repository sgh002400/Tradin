package com.traders.traders.common.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BaseException.class)
	protected ResponseEntity<ExceptionResponse> handleExpectedException(BaseException e) {
		log.error("Expected exception - code: {}, message: {}", e.getErrorCode(), e.getMessage(), e);
		ExceptionResponse response = ExceptionResponse.builder()
			.httpStatus(e.getHttpStatus())
			.errorCode(e.getErrorCode())
			.message(e.getMessage())
			.build();

		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException e) {
		log.error("Constraint violation exception", e);

		ExceptionResponse response = ExceptionResponse.builder()
			.errorCode(e.toString())
			.httpStatus(HttpStatus.BAD_REQUEST)
			.message(e.getMessage())
			.build();

		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<ExceptionResponse> handleUnexpectedRuntimeException(RuntimeException e) {
		log.error("Unexpected runtime exception", e);
		ExceptionResponse response = ExceptionResponse.builder()
			.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
			.errorCode("UNEXPECTED_RUNTIME_EXCEPTION")
			.message("Internal Server Error")
			.build();

		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@ExceptionHandler(Throwable.class)
	protected ResponseEntity<ExceptionResponse> handleAllExceptions(Throwable e) {
		log.error("Unexpected exception", e);
		ExceptionResponse response = ExceptionResponse.builder()
			.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
			.errorCode("UNEXPECTED_EXCEPTION")
			.message("Internal Server Error")
			.build();

		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
		HttpStatus status, WebRequest request) {
		log.error("Internal exception", ex);
		ExceptionResponse response = ExceptionResponse.builder()
			.httpStatus(status)
			.errorCode(ex.getClass().getSimpleName())
			.message(ex.getMessage())
			.build();

		return new ResponseEntity<>(response, response.getHttpStatus());
	}
}
