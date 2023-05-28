package com.traders.traders.common.utils;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.traders.traders.common.exception.TradersException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {
	public static Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			throw new TradersException(NOT_FOUND_SECURITY_CONTEXT_EXCEPTION);
		}

		return Long.valueOf(authentication.getName());
	}
}
