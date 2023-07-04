package com.tradin.module.auth.controller.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tradin.module.auth.service.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenRequestDto {
    private String idToken;
    private String accessToken;
    private String refreshToken;
    private String expiresIn;
    private String tokenType;

    public TokenDto toServiceDto() {
        return TokenDto.of(idToken, accessToken, refreshToken, expiresIn, tokenType);
    }
}
