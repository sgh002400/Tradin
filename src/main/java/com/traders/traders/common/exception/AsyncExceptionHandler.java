package com.traders.traders.common.exception;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
		log.error("Async Exception: {}", throwable.getMessage(), throwable);
	}
}
