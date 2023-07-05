package com.tradin.common.secret;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@RequiredArgsConstructor
@ConstructorBinding
@Getter
@ConfigurationProperties(prefix = "secret")
public final class SecretKeyManager {
    private final String cognitoClientId;
    private final String cognitoAuthRedirectUri;
    private final String cognitoIssuer;
}