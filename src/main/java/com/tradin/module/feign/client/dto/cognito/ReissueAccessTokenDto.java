package com.tradin.module.feign.client.dto.cognito;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ReissueAccessTokenDto {
    private String grantType;
    private String clientId;
    private String redirectUri;
}
