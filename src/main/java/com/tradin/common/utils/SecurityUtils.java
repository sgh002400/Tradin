package com.tradin.common.utils;

import com.tradin.common.exception.ExceptionMessage;
import com.tradin.common.exception.TradinException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {
    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new TradinException(ExceptionMessage.NOT_FOUND_SECURITY_CONTEXT_EXCEPTION);
        }

        return Long.valueOf(authentication.getName());
    }
}
