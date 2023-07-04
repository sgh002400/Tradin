package com.tradin.module.feign.client;

import com.tradin.module.feign.client.dto.cognito.AuthDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cognitoClient", url = "https://tradin.auth.ap-northeast-2.amazoncognito.com")
public interface CognitoClient {
    @PostMapping("/oauth2/token")
    void getAccessAndRefreshToken(@RequestBody AuthDto authDto);
}
