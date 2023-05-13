package com.traders.traders.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.traders.traders.common.secret.SecretKeyManager;

@Configuration
@EnableConfigurationProperties(value = {SecretKeyManager.class})
public class SecretPropertiesConfiguration {
}