package com.tradin.common.config;

import com.tradin.common.secret.SecretKeyManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {SecretKeyManager.class})
public class SecretPropertiesConfiguration {
}