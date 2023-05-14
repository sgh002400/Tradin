package com.traders.traders.common.exception;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	@Override //TODO - 해결하기
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
		String params = Arrays.stream(obj)
			.map(Object::toString)
			.collect(Collectors.joining(", "));
		log.error("Unexpected error occurred invoking async method: " + method + " with params: " + params, throwable);
	}
}
