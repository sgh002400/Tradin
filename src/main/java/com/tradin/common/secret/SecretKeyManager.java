package com.tradin.common.secret;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "secret")
public final class SecretKeyManager {
    private final String jwt;
}