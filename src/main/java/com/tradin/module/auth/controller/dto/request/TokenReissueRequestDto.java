package com.tradin.module.auth.controller.dto.request;

import com.tradin.module.auth.service.dto.TokenReissueDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class TokenReissueRequestDto {
    @NotBlank(message = "RefreshToken must not be blank")
    private final String refreshToken;

    public TokenReissueDto toServiceDto() {
        return TokenReissueDto.of(refreshToken);
    }
}
