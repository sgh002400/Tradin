package com.tradin.common.secret;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@ConstructorBinding
@Getter
@Validated
@ConfigurationProperties(prefix = "secret")
public final class SecretKeyManager {
    @NotBlank
    private final String cognitoClientId;

    @NotBlank
    private final String cognitoAuthRedirectUri;

    @NotBlank
    private final String cognitoIssuer;
}