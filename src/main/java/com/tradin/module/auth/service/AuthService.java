package com.tradin.module.auth.service;

import com.tradin.common.jwt.JwtUtil;
import com.tradin.module.auth.service.dto.UserDataDto;
import com.tradin.module.feign.client.dto.cognito.TokenDto;
import com.tradin.module.feign.service.CognitoFeignService;
import com.tradin.module.users.controller.dto.response.TokenResponseDto;
import com.tradin.module.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UsersService usersService;
    private final CognitoFeignService cognitoFeignService;
    private final JwtUtil jwtUtil;

    public TokenResponseDto auth(String code) {
        TokenDto token = cognitoFeignService.getAccessAndRefreshToken(code);

        UserDataDto userDataDto = extractUserDataFromIdToken(token.getIdToken());
        saveUser(userDataDto);

        return TokenResponseDto.of(token.getAccessToken(), token.getRefreshToken());
    }

    private String extractUserDataFromIdToken(String idToken) {
        return jwtUtil.getEmailFromIdToken(idToken);
    }

    private void saveUser(UserDataDto userDataDto) {
        usersService.saveUser(userDataDto);
    }

}
