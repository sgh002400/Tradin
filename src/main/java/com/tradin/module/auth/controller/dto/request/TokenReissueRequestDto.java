package com.tradin.module.auth.controller.dto.request;

import com.tradin.module.auth.service.dto.TokenReissueDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenReissueRequestDto {
    private final String refreshToken;

    public TokenReissueDto toServiceDto() {
        return TokenReissueDto.of(refreshToken);
    }
}
