package com.tradin.module.feign.service;

import com.tradin.common.secret.SecretKeyManager;
import com.tradin.module.feign.client.CognitoClient;
import com.tradin.module.feign.client.CognitoJwkFeignClient;
import com.tradin.module.feign.client.dto.cognito.AuthDto;
import com.tradin.module.feign.client.dto.cognito.JwkDtos;
import com.tradin.module.feign.client.dto.cognito.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CognitoFeignService {
    private final SecretKeyManager secretKeyManager;
    private final CognitoClient cognitoClient;
    private final CognitoJwkFeignClient cognitoJwkFeignClient;

    public TokenDto getAccessAndRefreshToken(String code) {
        final String COGNITO_CLIENT_ID = secretKeyManager.getCognitoClientId();
        final String COGNITO_AUTH_REDIRECT_URI = secretKeyManager.getCognitoAuthRedirectUri();

        AuthDto authDto = new AuthDto("authorization_code", COGNITO_CLIENT_ID, COGNITO_AUTH_REDIRECT_URI, code);

        return cognitoClient.getAccessAndRefreshToken(authDto);
    }

    public JwkDtos getJwkKey() {
        return cognitoJwkFeignClient.getJwks();
    }
}
