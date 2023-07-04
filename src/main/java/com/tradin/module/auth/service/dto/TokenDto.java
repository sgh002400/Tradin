package com.tradin.module.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private final String idToken;
    private final String accessToken;
    private final String refreshToken;
    private final String expiresIn;
    private final String tokenType;

    public static TokenDto of(String idToken, String accessToken, String refreshToken, String expiresIn, String tokenType) {
        return new TokenDto(idToken, accessToken, refreshToken, expiresIn, tokenType);
    }
}
